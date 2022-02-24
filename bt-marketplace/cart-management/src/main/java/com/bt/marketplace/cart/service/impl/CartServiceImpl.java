package com.bt.marketplace.cart.service.impl;

import com.bt.marketplace.cart.constants.ErrorMessagesImpl;
import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.entity.CartAttributes;
import com.bt.marketplace.cart.entity.CartAudit;
import com.bt.marketplace.cart.entity.CartPricing;
import com.bt.marketplace.cart.mapper.GetCartRequestEntityMapper;
import com.bt.marketplace.cart.model.*;
import com.bt.marketplace.cart.repository.CartEntityRepo;
import com.bt.marketplace.cart.service.CartService;
import com.bt.marketplace.cart.util.updatebuilder.CartUpdateBuilder;
import com.bt.marketplace.common.constants.CartChannels;
import com.bt.marketplace.common.constants.CartStatus;
import com.bt.marketplace.common.constants.StatusMethods;
import com.bt.marketplace.common.exception.ApiErrorResponseException;
import com.bt.marketplace.common.querymanager.UpdateQueryManager;
import com.bt.marketplace.common.util.SecurityUtils;
import com.bt.marketplace.common.util.UserTokenInfo;
import java.time.Instant;
import java.time.Instant;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private CartEntityRepo cartEntityRepo;

  @Autowired
  private ApplicationContext applicationContext;


  @Autowired
  private MongoOperations mongoOperations;

  @Override
  public CartCreationResponse save(CartCreationRequest cartRequest) {

    UserTokenInfo userTokenInfo = SecurityUtils.getUserTokenInfo();
    if (!CartChannels.isChannelPresent(cartRequest.getChannel())) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CHANNEL));
    }
    CartAttributes userAttributes = CartAttributes.builder()
        .name(cartRequest.getName()).userId(userTokenInfo.getUserId())
        .channel(cartRequest.getChannel()).checkoutRequired(true).build();

    CartAudit cartAudit = CartAudit.builder().createdTime(Instant.now())
        .createdBy(userTokenInfo.getUserEmail())
        .updatedTime(Instant.now()).updatedBy(userTokenInfo.getUserEmail()).build();
    CartEntity cartEntity = CartEntity.builder()
        .attributes(userAttributes)
        .status(CartStatus.PENDING.name())
        .type(cartRequest.getType())
        .cartAudit(cartAudit).build();
    cartEntity = cartEntityRepo.save(cartEntity);
    return CartCreationResponse.builder().cartId(cartEntity.getId()).build();

  }

  @Override
  public CartCreationResponse update(UpdateCartRequest updateCartRequest, String id) {

    Query query = Query.query(where(QUERY_ID).is(id));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    if (ObjectUtils.isEmpty(cartEntity)) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_ID));
    }
    if (!checkCartStatus(cartEntity.getStatus())) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_STATUS)
      );
    }
    if (!CartChannels.isChannelPresent(updateCartRequest.getChannel())) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CHANNEL));
    }
    UpdateQueryManager<CartUpdateBuilder> updateQueryManager =
        applicationContext.getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, id);

    cartUpdateBuilder.setField(CART_ATTRIBUTE_NAME, updateCartRequest.getName());
    cartUpdateBuilder.setField(CART_TYPE, updateCartRequest.getType());
    cartUpdateBuilder.setField(CART_CHANNEL, updateCartRequest.getChannel());
    cartUpdateBuilder.setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, true);
    updateQueryManager.executeUpdates();

    return CartCreationResponse.builder().cartId(id).build();

  }

  @Override
  public void delete(String id) {

    Query query = Query.query(where(QUERY_ID).is(id));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    if (ObjectUtils.isEmpty(cartEntity)) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_ID));
    }
     if (!cartEntity.getStatus().equals(CartStatus.PENDING.name())) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_STATUS));
    }
    UpdateQueryManager<CartUpdateBuilder> updateQueryManager =
        applicationContext.getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, id);
    cartUpdateBuilder.setField(CART_STATUS, CartStatus.DELETED.toString());
    updateQueryManager.executeUpdates();

  }

  @Override
  public CartEntity getCartEntity(String id, boolean getCartFlag) {

    Query query = Query.query(where("_id").is(id));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    if (ObjectUtils.isEmpty(cartEntity)) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_ID));
    }
    if (getCartFlag) {
      calculatePricing(cartEntity, id);
    }

    return cartEntity;
  }

  @Override
  public GetCartResponse getCart(String id, boolean getCartFlag) {

    CartEntity cartEntity = getCartEntity(id, getCartFlag);
    GetCartRequestEntityMapper getCartRequestEntityMapper = Mappers
        .getMapper((GetCartRequestEntityMapper.class));
    GetCartResponse getCartResponse = getCartRequestEntityMapper.entityToResponse(cartEntity);

    return getCartResponse;
  }

  @Override
  public GetCartVerificationResponse getCartStatus(String id) {

    Query query = Query.query(where("_id").is(id));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    if (ObjectUtils.isEmpty(cartEntity)) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_ID));
    }
    return GetCartVerificationResponse.builder().id(id).status(cartEntity.getStatus()).build();

  }



  private void calculatePricing(CartEntity cartEntity, String id) {

    UpdateQueryManager<CartUpdateBuilder> updateQueryManager =
        applicationContext.getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, id);
    cartUpdateBuilder.setUpsertFirst(true);
    CartPricing cartPricing = CartPricing.builder().subTotal(BigDecimal.ZERO)
        .total(BigDecimal.ZERO).build();
    if (ObjectUtils.isEmpty(cartEntity.getCartItems())){
      return;
    }
    cartEntity.getCartItems().forEach(cartItem -> {
      cartItem.setOfferPrice(cartItem.getSalesPrice());
      cartItem.setSubTotal(cartItem.getOfferPrice().multiply(cartItem.getQuantity()));
      cartItem.setTotal(cartItem.getSubTotal());
      cartPricing.setSubTotal(cartPricing.getSubTotal().add(cartItem.getSubTotal()));
      cartPricing.setTotal(cartItem.getTotal().add(cartPricing.getTotal()));
      cartUpdateBuilder.setItemField(cartItem.getCartItemId(), CART_ITEM_OFFER_PRICE,
          cartItem.getOfferPrice());
      cartUpdateBuilder.setItemField(cartItem.getCartItemId(), CART_ITEM_SUBTOTAL,
          cartItem.getSubTotal());
      cartUpdateBuilder.setItemField(cartItem.getCartItemId(), CART_ITEM_TOTAL,
          cartItem.getTotal());
    });
    cartEntity.setCartPricing(cartPricing);
    cartUpdateBuilder.setField(CART_PRICING, cartPricing);
    updateQueryManager.executeUpdates();
  }


  @Override
  public void cartAttribute(UpdateCartAttributeRequest updateCartAttributeRequest, String id) {
    if(ObjectUtils.isEmpty(updateCartAttributeRequest)){return;}
    Query query = Query.query(where(QUERY_ID).is(id));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    if (ObjectUtils.isEmpty(cartEntity)) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_ID));
    }

    UpdateQueryManager<CartUpdateBuilder> updateQueryManager =
        applicationContext.getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, id);

    if(!ObjectUtils.isEmpty(updateCartAttributeRequest.getStatus())){
      if (cartEntity.getStatus().equals(CartStatus.DELETED.name())) {
        throw new ApiErrorResponseException(
            ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_STATUS));
      }

      if (!StatusMethods.isMethodPresent(updateCartAttributeRequest.getStatus())) {
        throw new ApiErrorResponseException(
            ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_STATUS));
      }
        cartUpdateBuilder.setField(CART_STATUS, updateCartAttributeRequest.getStatus());

    }
    if(!ObjectUtils.isEmpty(updateCartAttributeRequest.getCheckoutRequired())){
      cartUpdateBuilder.setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, updateCartAttributeRequest.getCheckoutRequired());
    }
    updateQueryManager.executeUpdates();
  }

  private String generateCartId(String id) {
    try {
      UpdateQueryManager<CartUpdateBuilder> updateQueryManager =
          applicationContext.getBean(UpdateQueryManager.class);
      CartUpdateBuilder cartUpdateBuilder =
          updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, id);

      UUID gen = UUID.randomUUID();
      cartUpdateBuilder.setField("cartId", gen.toString());
      updateQueryManager.executeUpdates();
      return gen.toString();
    } catch (Exception ex) {
      generateCartId(id);
    }
    return null;
  }

  private boolean checkCartStatus(String status) {
    return CartStatus.PENDING.name().equals(status);
  }

}

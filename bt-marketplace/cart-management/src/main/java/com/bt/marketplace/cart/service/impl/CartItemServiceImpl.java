package com.bt.marketplace.cart.service.impl;

import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.CART_ATTRIBUTE_CHECKOUT_REQUIRED;
import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.CART_ITEM_QUANTITY;
import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.CART_STATUS;
import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.QUERY_ID;
import static com.bt.marketplace.common.constants.DatabaseSequenceName.CART_ITEM_ID_SEQUENCE;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bt.marketplace.cart.constants.ErrorMessagesImpl;
import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.entity.CartItem;
import com.bt.marketplace.cart.model.ItemCreationRequest;
import com.bt.marketplace.cart.model.ItemCreationResponse;
import com.bt.marketplace.cart.model.QuantityUpdateRequest;
import com.bt.marketplace.cart.service.CartItemService;
import com.bt.marketplace.cart.util.helper.ItemUpdateHelper;
import com.bt.marketplace.cart.util.updatebuilder.CartUpdateBuilder;
import com.bt.marketplace.common.constants.CartStatus;
import com.bt.marketplace.common.exception.ApiErrorResponseException;
import com.bt.marketplace.common.querymanager.UpdateQueryManager;
import com.bt.marketplace.common.util.SequenceGeneratorUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CartItemServiceImpl implements CartItemService {


  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private SequenceGeneratorUtils sequenceGeneratorUtils;

  @Autowired
  private MongoOperations mongoOperations;

  @Autowired
  private ItemUpdateHelper itemUpdateHelper;

  Provider<UpdateQueryManager<CartUpdateBuilder>> updateQueryManagerProvider;

  @Override
  public ItemCreationResponse save(ItemCreationRequest itemCartRequest, String cartId) {

    Query query = Query.query(where(QUERY_ID).is(cartId));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    validateCartStatus(cartEntity);
    UpdateQueryManager<CartUpdateBuilder> updateQueryManager = applicationContext
        .getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, cartId);
    CartItem cartItem;
    cartItem = checkItemSkuExists(itemCartRequest.getSku(), cartEntity);
    if (!ObjectUtils.isEmpty(cartItem)) {
      BigDecimal quantity = itemCartRequest.getQuantity();
      cartUpdateBuilder.setItemField(cartItem.getCartItemId(), CART_ITEM_QUANTITY,
          cartItem.getQuantity().add(quantity));
    } else {
      cartItem = CartItem.builder()
          .productId(itemCartRequest.getProductId())
          .name(itemCartRequest.getName())
          .quantity(itemCartRequest.getQuantity())
          .salesPrice(itemCartRequest.getSalesPrice())
          .sku(itemCartRequest.getSku())
          .cartItemId(sequenceGeneratorUtils.generateSequence(CART_ITEM_ID_SEQUENCE.name()))
          .build();

      cartUpdateBuilder.setField(CART_STATUS, CartStatus.PENDING.name());
      cartUpdateBuilder.setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, true);
      cartUpdateBuilder.addItem(cartItem);
    }
    updateQueryManager.executeUpdates();

    return ItemCreationResponse.builder()
        .cartItemId(cartItem.getCartItemId()).build();
  }

  @Override
  public ItemCreationResponse update(ItemCreationRequest itemCartRequest, String cartId,
      String cartItemId) {
    Query query = Query.query(where(QUERY_ID).is(cartId));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    validateCartStatus(cartEntity);
    if (!checkCartItemIdExists(cartEntity, Long.valueOf(cartItemId))) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_ITEM_ID));
    }

    UpdateQueryManager<CartUpdateBuilder> updateQueryManager = applicationContext
        .getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, cartId);

    cartUpdateBuilder.setItemField(Long.parseLong(cartItemId), "cartItems.$[cartItems{0}].name",
        itemCartRequest.getName());
    cartUpdateBuilder.setItemField(Long.parseLong(cartItemId),
        "cartItems.$[cartItems{0}].productId", itemCartRequest.getProductId());
    cartUpdateBuilder.setItemField(Long.parseLong(cartItemId), "cartItems.$[cartItems{0}].quantity",
        itemCartRequest.getQuantity());
    cartUpdateBuilder.setItemField(Long.parseLong(cartItemId), "cartItems.$[cartItems{0}].sku",
        itemCartRequest.getSku());
    cartUpdateBuilder.setItemField(Long.parseLong(cartItemId),
        "cartItems.$[cartItems{0}].salesPrice", itemCartRequest.getSalesPrice());

     cartUpdateBuilder.setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, true);
    updateQueryManager.executeUpdates();

    return ItemCreationResponse.builder().cartItemId(Long.parseLong(cartItemId)).build();

  }

  @Override
  public void delete(String cartId, String cartItemId) {
    CartEntity cartEntity = null;

    Query query = Query.query(where(QUERY_ID).is(cartId));
    cartEntity = mongoOperations.findOne(query, CartEntity.class);
    validateCartStatus(cartEntity);

    if (!checkCartItemIdExists(cartEntity, Long.parseLong(cartItemId))) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_ITEM_ID));
    }

    UpdateQueryManager<CartUpdateBuilder> updateQueryManager = applicationContext
        .getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, cartId);
    cartUpdateBuilder.removeItem(Long.parseLong(cartItemId));

    cartUpdateBuilder.setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, true);
    updateQueryManager.executeUpdates();
  }


  private boolean checkCartItemIdExists(CartEntity cartEntity, Long cartItemId) {
    if (ObjectUtils.isEmpty(cartEntity.getCartItems())) {
      return false;
    }
    return cartEntity.getCartItems().stream()
        .anyMatch(cartItem -> cartItemId.equals(cartItem.getCartItemId()));
  }

  private boolean checkCartStatus(String status) {
    return CartStatus.PENDING.name().equals(status);
  }

  private CartItem checkItemSkuExists(String sku, CartEntity cartEntity) {
    if (ObjectUtils.isEmpty(cartEntity.getCartItems())) {
      return null;
    }
    return cartEntity.getCartItems().stream()
        .filter(cartItem -> sku.equals(cartItem.getSku())).findFirst().orElse(null);
  }

  @Override
  public void deleteAll(String cartId) {

    UpdateQueryManager<CartUpdateBuilder> updateQueryManager = applicationContext
        .getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, cartId);

    Query query = Query.query(where(QUERY_ID).is(cartId));

    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    validateCartStatus(cartEntity);

    if (!ObjectUtils.isEmpty(cartEntity.getCartItems())) {
      List<Long> cartItems = cartEntity.getCartItems().stream().map(CartItem::getCartItemId)
          .collect(Collectors.toList());

      cartUpdateBuilder.removeItems(cartItems);
      cartUpdateBuilder.setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, true);
      updateQueryManager.executeUpdates();
    }
  }

  @Override
  public void updateQuantity(QuantityUpdateRequest updateRequest, String cartId, String cartItemId) {

    Query query = Query.query(where(QUERY_ID).is(cartId));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    validateCartStatus(cartEntity);
    if (!checkCartItemIdExists(cartEntity, Long.parseLong(cartItemId))) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_ITEM_ID));
    }
    UpdateQueryManager<CartUpdateBuilder> updateQueryManager =
        applicationContext.getBean(UpdateQueryManager.class);
    CartUpdateBuilder cartUpdateBuilder =
        updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, cartId);

    cartUpdateBuilder.setItemField(Long.parseLong(cartItemId), CART_ITEM_QUANTITY,
        updateRequest.getQuantity());
    cartUpdateBuilder.setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, true);
    updateQueryManager.executeUpdates();
  }

  private void validateCartStatus(CartEntity cartEntity) {
    if (ObjectUtils.isEmpty(cartEntity)) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_ID));
    }
    if (!checkCartStatus(cartEntity.getStatus())) {
      throw new ApiErrorResponseException(
          ErrorMessagesImpl.getApiError(ErrorMessagesImpl.INVALID_CART_STATUS)
      );
    }
  }
}

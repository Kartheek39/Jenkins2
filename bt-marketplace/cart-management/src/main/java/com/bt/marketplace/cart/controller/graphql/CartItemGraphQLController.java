package com.bt.marketplace.cart.controller.graphql;


import com.bt.marketplace.cart.model.ItemCreationRequest;
import com.bt.marketplace.cart.model.ItemCreationResponse;
import com.bt.marketplace.cart.model.QuantityUpdateRequest;
import com.bt.marketplace.cart.model.UpdateResponse;
import com.bt.marketplace.cart.service.CartItemService;
import com.bt.marketplace.cart.service.impl.CartServiceImpl;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CartItemGraphQLController {


  @Autowired
  private CartItemService cartItemService;

  private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

  @MutationMapping(name = "addCartItem")
  public ItemCreationResponse addCartItem(@Argument @Valid ItemCreationRequest itemCreationRequest,
      @Argument String cartId) {
    log.debug("REST request : {}", itemCreationRequest);
    ItemCreationResponse result = cartItemService.save(itemCreationRequest, cartId);
    return result;

  }

  @MutationMapping(name = "updateCartItem")
  public ItemCreationResponse updateCartItem(@Argument @Valid
      ItemCreationRequest itemCreationRequest, @Argument String cartId, @Argument String cartItemId) {

    log.debug("REST request  : {}", itemCreationRequest);
    ItemCreationResponse result = cartItemService.update(itemCreationRequest, cartId, cartItemId);
    return result;

  }


  @MutationMapping(name = "updateCartItemQuantity")
  public UpdateResponse updateCartItemQuantity(
      @Argument @Valid QuantityUpdateRequest quantityUpdateRequest,
      @Argument String cartId, @Argument String cartItemId) {

    log.debug("REST request  : {}", quantityUpdateRequest);
    cartItemService.updateQuantity(quantityUpdateRequest, cartId, cartItemId);
    return UpdateResponse.builder().status("SUCCESS").build();

  }

  @MutationMapping("deleteCartItem")
  public UpdateResponse deleteCartItem(@Argument String cartId, @Argument String cartItemId) {

    cartItemService.delete(cartId, cartItemId);
    return UpdateResponse.builder().status("SUCCESS").build();

  }

  @MutationMapping("deleteCartItems")
  public UpdateResponse deleteCartItems(@Argument String cartId) {

    cartItemService.deleteAll(cartId);
    return UpdateResponse.builder().status("SUCCESS").build();

  }

}

package com.bt.marketplace.cart.controller.graphql;


import com.bt.marketplace.cart.model.*;
import com.bt.marketplace.cart.service.CartService;
import com.bt.marketplace.cart.service.impl.CartServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class CartGraphQLController {

  private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

  @Autowired
  private CartService cartService;


  @QueryMapping(name = "getCart")
  public GetCartResponse getCart(@Argument String cartId) {
    GetCartResponse getCartResponse = cartService.getCart(cartId,true);
    return getCartResponse;
  }

  @QueryMapping(name = "getCartStatus")
  public GetCartVerificationResponse getCartOperation(@Argument String cartId) {
    GetCartVerificationResponse result = cartService.getCartStatus(cartId);
    log.info("CartEntity: ", result);
    return result;
  }

  @MutationMapping(name = "createCart")
  public CartCreationResponse createCart( @Valid @Argument CartCreationRequest cartCreationRequest) {

    log.debug("REST request  : {}", cartCreationRequest);
    return cartService.save(cartCreationRequest);
  }

  @MutationMapping(name = "updateCart")
  public CartCreationResponse updateCart( @Valid @Argument UpdateCartRequest updateCartRequest,
      @Argument String cartId) {

    log.debug("REST request : {}", updateCartRequest);
    return cartService.update(updateCartRequest, cartId);
  }

  @MutationMapping(name = "deleteCart")
  public UpdateResponse deleteCart( @Valid @Argument String cartId) {
    cartService.delete(cartId);
    return UpdateResponse.builder().status("SUCCESS").build();
  }


  @MutationMapping(name = "updateCartAttribute")
  public UpdateResponse updateCartAttribute(@Valid @Argument UpdateCartAttributeRequest updateCartAttributeRequest,
      @Valid @Argument String cartId){
    log.debug("Checkout attribute model  : {}", updateCartAttributeRequest);
    cartService.cartAttribute(updateCartAttributeRequest,cartId);
    return UpdateResponse.builder().status("SUCCESS").build();
  }
}

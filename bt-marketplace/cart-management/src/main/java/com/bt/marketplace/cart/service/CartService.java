package com.bt.marketplace.cart.service;


import com.bt.marketplace.cart.model.CartCreationRequest;
import com.bt.marketplace.cart.model.CartCreationResponse;
import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.model.GetCartResponse;
import com.bt.marketplace.cart.model.GetCartVerificationResponse;
import com.bt.marketplace.cart.model.UpdateCartAttributeRequest;
import com.bt.marketplace.cart.model.UpdateCartRequest;

public interface CartService {

    CartCreationResponse save(CartCreationRequest cartRequest);

    CartCreationResponse update(UpdateCartRequest updateCartRequest, String id);

    void delete(String id);

    CartEntity getCartEntity(String id, boolean getCartFlag);

    GetCartResponse getCart(String id, boolean getCartFlag);

    GetCartVerificationResponse getCartStatus(String id);

    void cartAttribute(UpdateCartAttributeRequest updateCartAttributeRequest,String id);
}

package com.bt.marketplace.cart.service;

import com.bt.marketplace.cart.model.ItemCreationRequest;
import com.bt.marketplace.cart.model.ItemCreationResponse;
import com.bt.marketplace.cart.model.QuantityUpdateRequest;

public interface CartItemService {
    ItemCreationResponse save(ItemCreationRequest itemCartRequest, String id);

    ItemCreationResponse update(ItemCreationRequest itemCartRequest, String id, String cartItemId);

    void delete(String cartId, String cartItemId);

    void deleteAll(String cartId);

    void updateQuantity(QuantityUpdateRequest updateRequest, String id, String cartItemId);
}



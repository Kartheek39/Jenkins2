package com.bt.marketplace.cart.util.updatebuilder;


import com.bt.marketplace.cart.entity.CartItem;
import java.util.List;

/**
 * interface for defining updates on CartItem array
 */
public interface CartItemUpdate<T> {

  T addItem(CartItem item) ;

  T removeItem(Long cartItemId);

  T removeItems(List<Long> cartItemIds);

  T setItemField(Long cartItemId, String fieldKey, Object value);

  T removeItemField(Long cartItemId, String fieldKey);
}

package com.bt.marketplace.cart.mapper;

import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.model.GetCartResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class GetCartResponseMapper {

  public GetCartResponse entityToResponse(CartEntity result) {
    if (ObjectUtils.isEmpty(result)) {
      return null;
    }
    return GetCartResponse.builder()
        .id(!ObjectUtils.isEmpty(result.getId()) ? result.getId() : null)
        .attributes(!ObjectUtils.isEmpty(result.getAttributes()) ? result.getAttributes() : null)
         .cartItems(!ObjectUtils.isEmpty(result.getCartItems()) ? result.getCartItems() : null)
        .cartPricing(
            !ObjectUtils.isEmpty(result.getCartPricing()) ? result.getCartPricing() : null)
        .type(!ObjectUtils.isEmpty(result.getType()) ? result.getType() : null)
        .status(!ObjectUtils.isEmpty(result.getStatus()) ? result.getStatus() : null)
        .build();
  }
}

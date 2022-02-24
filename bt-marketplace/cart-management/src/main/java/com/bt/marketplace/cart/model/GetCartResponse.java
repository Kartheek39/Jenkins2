package com.bt.marketplace.cart.model;

import com.bt.marketplace.cart.entity.CartAttributes;
import com.bt.marketplace.cart.entity.CartItem;
import com.bt.marketplace.cart.entity.CartPricing;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCartResponse {
  private String id;
  private String status;
  private String type;
  private CartAttributes attributes;
  private ArrayList<CartItem> cartItems;
  private CartPricing cartPricing;
}

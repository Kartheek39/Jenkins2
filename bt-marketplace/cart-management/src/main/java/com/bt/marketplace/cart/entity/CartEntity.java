package com.bt.marketplace.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {
    @Id
    private String id;
    private String status;
    private String type;
    private CartAttributes attributes;
    private ArrayList<CartItem> cartItems;
    private CartPricing cartPricing;
    private CartAudit cartAudit;
}

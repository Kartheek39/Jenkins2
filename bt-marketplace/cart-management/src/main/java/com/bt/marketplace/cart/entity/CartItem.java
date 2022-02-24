package com.bt.marketplace.cart.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItem {

    @JsonProperty("cartItemId")
    public Long cartItemId;
    @JsonProperty("productId")
    public String productId;
    @JsonProperty("quantity")
    public BigDecimal quantity;
    @JsonProperty("sku")
    public String sku;
    @JsonProperty("name")
    public String name;
    @JsonProperty("salesPrice")
    public BigDecimal salesPrice;
    @JsonProperty("offerPrice")
    public BigDecimal offerPrice;
    @JsonProperty("subTotal")
    public BigDecimal subTotal;
    @JsonProperty("total")
    public BigDecimal total;
}

package com.bt.marketplace.cart.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartAttributes {


    @JsonProperty("userId")
    public String userId;
    @JsonProperty("name")
    public String name;
    @JsonProperty("channel")
    public String channel;
    @JsonProperty("checkoutRequired")
    public Boolean checkoutRequired;
}

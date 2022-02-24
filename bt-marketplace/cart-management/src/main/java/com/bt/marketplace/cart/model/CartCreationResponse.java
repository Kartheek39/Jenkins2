package com.bt.marketplace.cart.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response model for Create and Update Cart Api")
public class CartCreationResponse {

  @Schema(name = "cartId", example="61d584a12e4a0c0cfed2a3f6", description = "Returns cartId of created cart")
  private String cartId;
}

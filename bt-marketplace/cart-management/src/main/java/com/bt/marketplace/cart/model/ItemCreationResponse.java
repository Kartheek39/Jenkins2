package com.bt.marketplace.cart.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response model for add and update item to cart Api")
public class ItemCreationResponse {

  @Schema(name = "cartItemId", example = "3928382", description = "returns cartItemId for that item")
  private Long cartItemId;
}

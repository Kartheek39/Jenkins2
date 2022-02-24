package com.bt.marketplace.cart.model;

import static com.bt.marketplace.cart.constants.ErrorMessagesImpl.INVALID_CART_CHANNEL;
import static com.bt.marketplace.cart.constants.ErrorMessagesImpl.INVALID_CART_NAME;
import static com.bt.marketplace.cart.constants.ErrorMessagesImpl.INVALID_CART_TYPE;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request model for Create Cart Api")
public class CartCreationRequest {

  @Schema(name = "name", example = "Test", description = "name of the cart")
  private String name;
  @Schema(name = "type", example = "SoftGood", description = "type of the cart")
  private String type;
  @NotBlank(message = INVALID_CART_CHANNEL)
  @Schema(name = "channel", example = "Marketplace", description = "channel of the cart")
  private String channel;
}

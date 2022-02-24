package com.bt.marketplace.cart.model;

import static com.bt.marketplace.cart.constants.ErrorMessagesImpl.INVALID_CART_CHANNEL;

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
@Schema(description = "Request model for Update Cart Api")
public class UpdateCartRequest {

  @Schema(name = "name", example = "Digital", description = "Update name")
  private String name;

  @Schema(name = "type", example = "DIGITAL", description = "Update type")
  private String type;

  @NotBlank(message = INVALID_CART_CHANNEL)
  @Schema(name = "channel", example = "Marketplace", description = "channel of the cart")
  private String channel;
}

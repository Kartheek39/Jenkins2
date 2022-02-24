package com.bt.marketplace.cart.model;

import static com.bt.marketplace.cart.constants.ErrorMessagesImpl.INVALID_ITEM_QUANTITY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Request model for update quantity api")
public class QuantityUpdateRequest {

  @Positive(message = INVALID_ITEM_QUANTITY)
  @Schema(name = "quantity", example = "2", description = "quantity that to be updated to the item")
  private BigDecimal quantity;
}

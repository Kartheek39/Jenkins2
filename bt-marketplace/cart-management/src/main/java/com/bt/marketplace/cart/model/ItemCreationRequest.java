package com.bt.marketplace.cart.model;


import com.bt.marketplace.cart.constants.ErrorMessagesImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Request model for Add Item to cart Api")
public class ItemCreationRequest {

  @NotBlank(message = ErrorMessagesImpl.INVALID_ITEM_PRODUCT_ID)
  @Schema(name = "productId", example = "047e3569-6889-4feb-9a22-1de5722a1a54", description = "productId of the item")
  private String productId;
  @Positive(message = ErrorMessagesImpl.INVALID_ITEM_QUANTITY)
  @Schema(name = "quantity", example = "1", description = "quantity of the item")
  private BigDecimal quantity;
  @NotBlank(message = ErrorMessagesImpl.INVALID_ITEM_SKU)
  @Schema(name = "sku", example = "SLACK", description = "sku of the item")
  private String sku;
  @NotBlank(message = ErrorMessagesImpl.INVALID_ITEM_NAME)
  @Schema(name = "name", example = "Slack", description = "name of the item")
  private String name;
  @Positive(message = ErrorMessagesImpl.INVALID_SALES_PRICE)
  @Schema(name = "salesPrice", example = "100", description = "price of the item")
  private BigDecimal salesPrice;
}

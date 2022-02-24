package com.bt.marketplace.cart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Java model class for providing shipping method to cart
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request model for Add Shipping Method Api")
public class UpdateCartAttributeRequest {

  @JsonProperty("checkoutRequired")
  public Boolean checkoutRequired;

  @JsonProperty("status")
  public String status;

}

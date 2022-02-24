package com.bt.marketplace.cart.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import graphql.GraphQLError;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Generic Response for Cart-Management  Api")
public class UpdateResponse{@Schema(name = "status", example = "SUCCESS",description = "generic response status field")
  private String status;
  private List<GraphQLError> errors;
}

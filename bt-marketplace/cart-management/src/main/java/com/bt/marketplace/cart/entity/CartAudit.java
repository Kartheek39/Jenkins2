package com.bt.marketplace.cart.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartAudit {
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("createdTime")
    private Instant createdTime;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("updatedTime")
    private Instant updatedTime;
    @JsonProperty("submittedBy")
    private String submittedBy;
    @JsonProperty("submittedTime")
    private Instant submittedTime;
}

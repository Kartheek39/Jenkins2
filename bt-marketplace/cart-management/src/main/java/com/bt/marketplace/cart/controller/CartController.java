package com.bt.marketplace.cart.controller;


import com.bt.marketplace.cart.model.UpdateCartAttributeRequest;
import com.bt.marketplace.cart.model.GetCartVerificationResponse;
import com.bt.marketplace.common.exception.ApiErrorResponse;
import com.bt.marketplace.cart.mapper.GetCartResponseMapper;
import com.bt.marketplace.cart.model.CartCreationRequest;
import com.bt.marketplace.cart.model.CartCreationResponse;
import com.bt.marketplace.cart.model.GetCartResponse;
import com.bt.marketplace.cart.model.UpdateCartRequest;
import com.bt.marketplace.cart.model.UpdateResponse;
import com.bt.marketplace.cart.service.CartService;
import com.bt.marketplace.cart.service.impl.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class CartController {

  private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

  @Autowired
  private CartService cartService;

  @Autowired
  private GetCartResponseMapper getCartResponseMapper;

  @PostMapping("/cart")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successfully created"),
      @ApiResponse(responseCode = "500", description = "Internal Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized client",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Forbidden",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
  @Operation(summary = "Cart Creation", description = "Creating the cart.",
      tags = {"cart"})
  public ResponseEntity<CartCreationResponse> createCart(
      @Parameter(description = "cart creation request model") @Valid @RequestBody
          CartCreationRequest cartRequest) {

    log.debug("REST request for cart creation : {}", cartRequest);
    CartCreationResponse result = cartService.save(cartRequest);
    return ResponseEntity.ok().body(result);

  }

  @PutMapping("/cart/{cart-id}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successfully created"),
      @ApiResponse(responseCode = "500", description = "Internal Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized client",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Forbidden",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
  @Operation(summary = "Updating Cart", description = "Updating the cart.",
      tags = {"cart"})
  public ResponseEntity<CartCreationResponse> updateCart(
      @Parameter(description = "updating cart request model")@Valid @RequestBody
          UpdateCartRequest updateCartRequest,
      @Parameter(example = "61d5c8dbdabc5865979f8285",
          description = "updating cart request model")
      @PathVariable("cart-id") String id) {

    log.debug("REST request : {}", updateCartRequest);
    CartCreationResponse result = cartService.update(updateCartRequest, id);
    return ResponseEntity.ok().body(result);

  }


  @DeleteMapping("/cart/{cart-id}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successfully created"),
      @ApiResponse(responseCode = "500", description = "Internal Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized client",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Forbidden",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
  @Operation(summary = "Deleting Cart", description = "Deleting the cart.",
      tags = {"cart"})
  public ResponseEntity<UpdateResponse> deleteCart( @Parameter(example = "61d5c8dbdabc5865979f8285",
      description = "Deleting cart") @PathVariable("cart-id") String id) {

    cartService.delete(id);
    return ResponseEntity.ok().body(UpdateResponse.builder().status("SUCCESS").build());

  }

  @GetMapping("/cart/{cart-id}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successfully created"),
      @ApiResponse(responseCode = "500", description = "Internal Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized client",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Forbidden",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
  @Operation(summary = "Get cart details", description = "Get the cart details.",
      tags = {"cart"})
  public ResponseEntity<GetCartResponse> getCart(@Parameter(example = "61ded48d917f763cd365d459",
      description = "Getting cart details") @PathVariable("cart-id") String id) {

    GetCartResponse getCartResponse = cartService.getCart(id,true);

    return ResponseEntity.ok().body(getCartResponse);

  }

  @GetMapping("/cart/{cart-id}/status")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successfully created"),
      @ApiResponse(responseCode = "500", description = "Internal Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized client",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Forbidden",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
  @Operation(summary = "Get cart status", description = "Get the cart status.",
      tags = {"cart"})
  private ResponseEntity<GetCartVerificationResponse> getCartStatus(@Parameter(example = "61ded48d917f763cd365d459",
      description = "Getting cart status") @PathVariable ("cart-id") String id){

    GetCartVerificationResponse getCartVerificationResponse = cartService.getCartStatus(id);

    return ResponseEntity.ok().body(getCartVerificationResponse);
  }

  @PostMapping("/cart/{cart-id}/attribute")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful"),
      @ApiResponse(responseCode = "500", description = "Internal Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized client",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Forbidden",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
  @Operation(summary = "update cart attributes", description = "Submit Cart",
      tags = {"cart"})
  public ResponseEntity<UpdateResponse> cartAttribute(
      @Parameter(description = "Cart Attribute Request")@RequestBody(required = false) UpdateCartAttributeRequest updateCartAttributeRequest,
      @Parameter(example = "61d5c8dbdabc5865979f8285",description = "cart-id")
  @PathVariable("cart-id") String id) {

    log.debug("Rest Request for CartAttribute  : {}", updateCartAttributeRequest);
    cartService.cartAttribute(updateCartAttributeRequest,id);
    return ResponseEntity.ok().body(UpdateResponse.builder().status("SUCCESS").build());

  }
}

package com.bt.marketplace.cart.controller;


import com.bt.marketplace.common.exception.ApiErrorResponse;
import com.bt.marketplace.cart.model.ItemCreationRequest;
import com.bt.marketplace.cart.model.ItemCreationResponse;
import com.bt.marketplace.cart.model.QuantityUpdateRequest;
import com.bt.marketplace.cart.model.UpdateResponse;
import com.bt.marketplace.cart.service.CartItemService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
@Validated
public class CartItemController {


  @Autowired
  private CartItemService cartItemService;

  private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

  @PostMapping("/cart/{cart-id}/item")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successfully added item to cart"),
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
  @Operation(summary = "Add Product to Cart", description = "Add Product to Cart.",
      tags = {"cart-item"})
  public ResponseEntity<ItemCreationResponse> createItem(
      @Valid @RequestBody @Parameter(description = "request model for adding an item") ItemCreationRequest itemCartRequest,
      @Parameter(description = "cart-id", example = "61d6aabc27c1d5720adf2e18") @PathVariable("cart-id") String cartId) {

    log.debug("REST request for adding item to given cartId : {}", itemCartRequest);
    ItemCreationResponse result = cartItemService.save(itemCartRequest, cartId);
    return ResponseEntity.ok().body(result);

  }

  @PutMapping("/cart/{cart-id}/item/{item-id}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successfully created"),
      @ApiResponse(responseCode = "500", description = "Internal Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request Error",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized client",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Forbidden",
          content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
  @Operation(summary = "Update item in cart", description = "Update item in cart added by the user.",
      tags = {"cart-item"})
  public ResponseEntity<ItemCreationResponse> updateItem(@Valid @RequestBody
      ItemCreationRequest itemCartRequest, @PathVariable("cart-id") String cartId,
      @PathVariable("item-id") String cartItemId) {

    log.debug("REST request  : {}", itemCartRequest);
    ItemCreationResponse result = cartItemService.update(itemCartRequest, cartId, cartItemId);
    return ResponseEntity.ok().body(result);

  }


  @PutMapping("/cart/{cart-id}/item/{item-id}/quantity")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successfully updated the quantity"),
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
  @Operation(summary = "Update Item Quantity Api", description = "Updates the item quantity for given cartItemId",
      tags = {"cart-item"})
  public ResponseEntity<UpdateResponse> updateQuantity(@Valid @RequestBody
  @Parameter(description = "Request model for update quantity api") QuantityUpdateRequest updateRequest,
      @Parameter(description = "cart-id", example = "61d6aabc27c1d5720adf2e18") @PathVariable(value = "cart-id") String cartId,
      @Parameter(description = "item-id", example = "3928306") @PathVariable("item-id") String cartItemId) {

    log.debug("REST request for update quantity of an item: {}", updateRequest);
    cartItemService.updateQuantity(updateRequest, cartId, cartItemId);
    return ResponseEntity.ok().body(UpdateResponse.builder().status("SUCCESS").build());

  }

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
  @Operation(summary = "Delete Cart Item Api", description = "Deletes the cart item with given cartItemId",
      tags = {"cart-item"})
  @DeleteMapping("/cart/{cart-id}/item/{item-id}")
  public ResponseEntity<UpdateResponse> deleteItem(@PathVariable(value = "cart-id",required = true) String cartId,
      @PathVariable(value = "item-id",required = true) String cartItemId) {

    cartItemService.delete(cartId, cartItemId);
    return ResponseEntity.ok().body(UpdateResponse.builder().status("SUCCESS").build());

  }


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
  @Operation(summary = "Delete All cartItems  Api", description = "Deletes all the cartItems in the cart",
      tags = {"cart-item"})

  @DeleteMapping("/cart/{cart-id}/item")
  public ResponseEntity<UpdateResponse> deleteItems(@PathVariable(value = "cart-id",required = true) String cartId) {

    cartItemService.deleteAll(cartId);
    return ResponseEntity.ok().body(UpdateResponse.builder().status("SUCCESS").build());
  }

}

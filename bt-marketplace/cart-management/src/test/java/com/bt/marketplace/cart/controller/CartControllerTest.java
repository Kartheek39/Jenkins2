package com.bt.marketplace.cart.controller;

import com.bt.marketplace.cart.model.CartCreationRequest;
import com.bt.marketplace.cart.model.CartCreationResponse;
import com.bt.marketplace.cart.model.UpdateCartRequest;
import com.bt.marketplace.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CartControllerTest {

  @Mock
  private CartService cartService;

  @InjectMocks
  private CartController cartController;

  @Test
  void testCartCreation() throws IOException {
    CartCreationRequest cartCreationRequest = cartCreateRequestJsonReader();
    CartCreationResponse cartCreationResponse = cartCreateResponseJsonReader();
    Mockito.when(cartService.save(cartCreationRequest)).thenReturn(cartCreationResponse);
    Assertions.assertEquals(ResponseEntity.ok().build().getStatusCode(),
        cartController.createCart(cartCreationRequest).getStatusCode());
  }

  @Test
  void testUpdateCart() throws IOException {
    UpdateCartRequest updateCartRequest = cartUpdateRequestJsonReader();
    CartCreationResponse cartCreationResponse = cartCreateResponseJsonReader();
    Mockito.when(cartService.update(updateCartRequest,"61d802d3ae05c521b9604c27"))
        .thenReturn(cartCreationResponse);
    Assertions.assertEquals(ResponseEntity.ok().build().getStatusCode(),
        cartController.updateCart(updateCartRequest,"61d802d3ae05c521b9604c27").getStatusCode());
  }

  @Test
  public void testDeleteCart() throws Exception {
    Mockito.doNothing().when(cartService).delete("61d802d3ae05c521b9604c27");
    Assertions.assertDoesNotThrow(()->cartController.deleteCart("61d802d3ae05c521b9604c27"));

  }

  public CartCreationRequest cartCreateRequestJsonReader() {
    InputStream in = CartControllerTest.class.getResourceAsStream(
        "src/test/resources/cartCreateRequest.json");
    if (in != null) {
      ObjectMapper object = new ObjectMapper();
      try {
        CartCreationRequest creationRequest = object.readValue(in, CartCreationRequest.class);
        return creationRequest;
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    return null;
  }

  public UpdateCartRequest cartUpdateRequestJsonReader() {
    InputStream in = CartControllerTest.class.getResourceAsStream(
        "src/test/resources/cartCreateRequest.json");
    if (in != null) {
      ObjectMapper object = new ObjectMapper();
      try {
        UpdateCartRequest updateCartRequest = object.readValue(in, UpdateCartRequest.class);
        return updateCartRequest;
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    return null;
  }

  public CartCreationResponse cartCreateResponseJsonReader() {
    InputStream in = CartControllerTest.class.getResourceAsStream(
        "src/test/resources/cartCreateResponse.json");
    if (in != null) {
      ObjectMapper object = new ObjectMapper();
      try {
        CartCreationResponse creationResponse = object.readValue(in,
            CartCreationResponse.class);
        return creationResponse;
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    return null;
  }
}

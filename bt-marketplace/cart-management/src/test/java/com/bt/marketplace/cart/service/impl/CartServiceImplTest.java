package com.bt.marketplace.cart.service.impl;

import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.mapper.GetCartRequestEntityMapper;
import com.bt.marketplace.cart.model.CartCreationRequest;
import com.bt.marketplace.cart.model.GetCartResponse;
import com.bt.marketplace.cart.model.UpdateCartAttributeRequest;
import com.bt.marketplace.cart.model.UpdateCartRequest;
import com.bt.marketplace.cart.repository.CartEntityRepo;
import com.bt.marketplace.cart.util.updatebuilder.CartUpdateBuilder;
import com.bt.marketplace.common.exception.ApiErrorResponseException;
import com.bt.marketplace.common.querymanager.UpdateQueryManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.io.InputStream;

import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.QUERY_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

  @InjectMocks
  private CartServiceImpl cartService;

  @Mock
  private UpdateQueryManager<CartUpdateBuilder> updateQueryManager;

  @Mock
  GetCartRequestEntityMapper getCartRequestEntityMapper;

  @Mock
  private ApplicationContext applicationContext;

  @Mock private CartEntityRepo cartEntityRepo;

  @Mock
  private MongoOperations mongoOperations;

  @Test
  void save() {
    CartEntity cartEntity = jsonCartEntityReader();
    CartCreationRequest cartRequest = CartCreationRequest.builder()
        .channel(jsonCartEntityReader().getAttributes().getChannel())
        .name(jsonCartEntityReader().getAttributes().getName())
        .type(jsonCartEntityReader().getType())
        .build();
    Mockito.lenient().when(cartEntityRepo.save(any())).thenReturn(cartEntity);
    assertDoesNotThrow(() -> cartService.save(cartRequest));
    Assertions.assertEquals("61d5c8dbdabc5865979f8285", cartEntity.getId());
  }

  @Test
  void testUpdate(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);

    UpdateCartRequest updateCartRequest = UpdateCartRequest.builder()
        .name(jsonCartEntityReader().getAttributes().getName())
        .type(jsonCartEntityReader().getType())
        .channel(jsonCartEntityReader().getAttributes().getChannel())
        .build();
    assertDoesNotThrow(() -> cartService.update(updateCartRequest, jsonCartEntityReader().getId()));
  }

  @Test
  void testUpdateIdException(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = new Query();
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);

    UpdateCartRequest updateCartRequest = UpdateCartRequest.builder()
        .name(jsonCartEntityReader().getAttributes().getName())
        .type(jsonCartEntityReader().getType())
        .build();
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.update(updateCartRequest, jsonCartEntityReader().getId()));
  }

  @Test
  void testUpdateChannelException(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);

    UpdateCartRequest updateCartRequest = UpdateCartRequest.builder()
        .name(jsonCartEntityReader().getAttributes().getName())
        .type(jsonCartEntityReader().getType())
        .channel(null)
        .build();
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.update(updateCartRequest, jsonCartEntityReader().getId()));
  }
  @Test
  void testUpdateStatusException(){
    CartEntity cartEntity = jsonCartEntityReader();
    cartEntity.setStatus("SUBMITTED");
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);

    UpdateCartRequest updateCartRequest = UpdateCartRequest.builder()
        .name(jsonCartEntityReader().getAttributes().getName())
        .type(jsonCartEntityReader().getType())
        .build();
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.update(updateCartRequest, jsonCartEntityReader().getId()));
  }
  @Test
  void testDeleteCart(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);

    assertDoesNotThrow(() ->cartService.delete(jsonCartEntityReader().getId()));
  }


  @Test
  void testDeleteCartException(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is(null));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
   CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);

    Assertions.assertThrows(ApiErrorResponseException.class,
        () ->cartService.delete(jsonCartEntityReader().getId()));
  }

  @Test
  void testDeleteCartStatusException(){
    CartEntity cartEntity = jsonCartEntityReader();
    cartEntity.setStatus("SUBMITTED");
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);

    Assertions.assertThrows(ApiErrorResponseException.class,
        () ->cartService.delete(jsonCartEntityReader().getId()));
  }

  @Test
  void saveChannelException() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = null;
    CartCreationRequest cartCreationRequest = new CartCreationRequest();
    Mockito.lenient().when(cartEntityRepo.save(cartEntity)).thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.save(cartCreationRequest));
  }

  @Test
  void cartAttributeTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    UpdateCartAttributeRequest updateCartAttributeRequest = UpdateCartAttributeRequest.builder()
        .status("PENDING")
        .checkoutRequired(false)
        .build();
    assertDoesNotThrow(() ->cartService.cartAttribute(updateCartAttributeRequest, jsonCartEntityReader().getId()));
  }
  @Test
  void cartAttributeCartIdExceptionTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is("1"));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    UpdateCartAttributeRequest updateCartAttributeRequest = UpdateCartAttributeRequest.builder()
        .status("PENDING")
        .checkoutRequired(false)
        .build();
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.cartAttribute(updateCartAttributeRequest, jsonCartEntityReader().getId()));
  }

  @Test
  void cartAttributeCartStatusExceptionTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    cartEntity.setStatus("DELETED");
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    UpdateCartAttributeRequest updateCartAttributeRequest = UpdateCartAttributeRequest.builder()
        .status("DELETED")
        .checkoutRequired(false)
        .build();
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.cartAttribute(updateCartAttributeRequest, jsonCartEntityReader().getId()));
  }


  @Test
  void cartAttributeCartInvalidStatusExceptionTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    cartEntity.setStatus("D");
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    UpdateCartAttributeRequest updateCartAttributeRequest = UpdateCartAttributeRequest.builder()
        .status("D")
        .checkoutRequired(false)
        .build();
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.cartAttribute(updateCartAttributeRequest, jsonCartEntityReader().getId()));
  }

  @Test
  void getCartEntityTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    assertDoesNotThrow(() ->cartService.getCartEntity(jsonCartEntityReader().getId(), true));
  }


  @Test
  void getCartEntityExceptionTest(){
    CartEntity cartEntity = null;
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.getCartEntity(jsonCartEntityReader().getId(), true));
  }

  @Test
  void getCartTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    GetCartResponse getCartResponse = GetCartResponse.builder().build();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    Mockito.lenient().when(getCartRequestEntityMapper.entityToResponse(cartEntity)).thenReturn(getCartResponse);
    assertDoesNotThrow(() ->cartService.getCart(jsonCartEntityReader().getId(),true));
  }

  @Test
  void getCartStatusTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    assertDoesNotThrow(() ->cartService.getCartStatus(jsonCartEntityReader().getId()));
  }
  @Test
  void getCartStatusExceptionTest(){
    CartEntity cartEntity = null;
    Query query = Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId()));
    Mockito.lenient().when(mongoOperations.findOne(query, CartEntity.class)).thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartService.getCartStatus(jsonCartEntityReader().getId()));
  }

  public CartEntity jsonCartEntityReader() {
    InputStream in = CartItemServiceImplTest.class.getResourceAsStream("/CartEntity.json");
    if (in != null) {
      ObjectMapper object = new ObjectMapper();
      try {
        CartEntity cartEntity = object.readValue(in, CartEntity.class);
        return cartEntity;
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    return null;
  }
}

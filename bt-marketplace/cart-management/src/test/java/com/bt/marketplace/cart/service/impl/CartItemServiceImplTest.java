package com.bt.marketplace.cart.service.impl;

import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.QUERY_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.entity.CartItem;
import com.bt.marketplace.common.exception.ApiErrorResponseException;
import com.bt.marketplace.cart.model.ItemCreationRequest;
import com.bt.marketplace.cart.model.QuantityUpdateRequest;
import com.bt.marketplace.cart.util.updatebuilder.CartUpdateBuilder;
import com.bt.marketplace.common.querymanager.UpdateQueryManager;
import com.bt.marketplace.common.util.SequenceGeneratorUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.inject.Provider;
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

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

  @InjectMocks
  private CartItemServiceImpl cartItemService;

  @Mock
  private UpdateQueryManager<CartUpdateBuilder> updateQueryManager;


  @Mock
  private ApplicationContext applicationContext;

  @Mock
  private SequenceGeneratorUtils sequenceGeneratorUtils;

  @Mock
  private MongoOperations mongoOperations;

  @Mock
  Provider<UpdateQueryManager<CartUpdateBuilder>> updateQueryManagerProvider;

  @Test
  void save() {
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is(jsonCartEntityReader().getId())), CartEntity.class))
        .thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    ItemCreationRequest itemCreationRequest = new ItemCreationRequest();
    itemCreationRequest.setSku("SKU");
    itemCreationRequest.setQuantity(BigDecimal.valueOf(2));
    Assertions.assertDoesNotThrow(() -> cartItemService.save(itemCreationRequest,
        jsonCartEntityReader().getId()));
  }


  @Test
  void saveEmptyCartItemTest() {
    ArrayList<CartItem> cartItem = null;
    CartEntity cartEntity = jsonCartEntityReader();
    cartEntity.setCartItems(cartItem);
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    ItemCreationRequest itemCreationRequest = new ItemCreationRequest();
    itemCreationRequest.setSku("SKU");
    itemCreationRequest.setQuantity(BigDecimal.valueOf(2));
    Assertions.assertDoesNotThrow(() -> cartItemService.save(itemCreationRequest, "123"));
  }

  @Test
  void saveStatusException() {
    CartEntity cartEntity = jsonCartEntityReader();
    cartEntity.setStatus("SUBMITTED");
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.save(new ItemCreationRequest(), "123"));
  }

  @Test
  void saveCartIdException() {
    CartEntity cartEntity = null;
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.save(new ItemCreationRequest(), ""));
  }

  @Test
  void updateTest() {
    ItemCreationRequest itemCreationRequest = new ItemCreationRequest();
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("cartId")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertDoesNotThrow(() -> cartItemService.update(itemCreationRequest,"cartId","3928311"));
  }

  @Test
  void updateExceptionTest() {
    ItemCreationRequest itemCreationRequest = new ItemCreationRequest();
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("cartId")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.update(itemCreationRequest,"cartId","1"));
  }

  @Test
  void delete() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();

    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertDoesNotThrow(() -> cartItemService.delete("123", "3928311"));


  }

  @Test
  void deleteCartIdException() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();

    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = null;
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.delete("123", "3928311"));


  }

  @Test
  void deleteCartItemInCartException() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();

    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = new CartEntity();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.delete("123", "3928311"));


  }

  @Test
  void deleteCartItemMissingException() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();

    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = new CartEntity();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.delete("123", ""));


  }

  @Test
  void deleteCartItemIdException() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManagerProvider.get())
        .thenReturn(updateQueryManager);
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.delete("123", "123"));


  }

  @Test
  void deleteAll() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();

    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertDoesNotThrow(() -> cartItemService.deleteAll("123"));

  }

  @Test
  void deleteAllThrowException() {
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();

    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    CartEntity cartEntity = null;
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.deleteAll("123"));

  }

  @Test
  void updateQuantity() {
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    Assertions.assertDoesNotThrow(() -> cartItemService.updateQuantity(new QuantityUpdateRequest(),"123", "3928311"));
  }

  @Test
  void updateQuantityStatusException() {
    CartEntity cartEntity = jsonCartEntityReader();
    cartEntity.setStatus("SUBMITTED");
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.updateQuantity(new QuantityUpdateRequest(), "123", "3928311"));
  }

  @Test
  void updateQuantityCartIdException() {
    CartEntity cartEntity = null;
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.updateQuantity(new QuantityUpdateRequest(), "", "3928311"));
  }

  @Test
  void updateQuantityItemIdException() {
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID).is("123")), CartEntity.class))
        .thenReturn(cartEntity);
    CartUpdateBuilder cartUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    Assertions.assertThrows(ApiErrorResponseException.class,
        () -> cartItemService.updateQuantity(new QuantityUpdateRequest(), "123", "1"));
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

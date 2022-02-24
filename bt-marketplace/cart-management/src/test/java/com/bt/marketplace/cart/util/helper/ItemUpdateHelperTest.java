package com.bt.marketplace.cart.util.helper;

import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.QUERY_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bt.marketplace.cart.entity.CartEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

@ExtendWith(MockitoExtension.class)
public class ItemUpdateHelperTest {
  @InjectMocks
  private ItemUpdateHelper itemUpdateHelper;

  @Mock
  private MongoOperations mongoOperations;

  @Test
  void validateItemIdForCartIdTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID)
            .is(jsonCartEntityReader().getId())
            .and("cartItems.cartItemId").is("3928311")), CartEntity.class))
              .thenReturn(cartEntity);
    Assertions.assertDoesNotThrow(() -> itemUpdateHelper.validateItemIdForCartId(
        jsonCartEntityReader().getId(), "3928311"));

  }
  @Test
  void validateItemIdTest(){
    CartEntity cartEntity = jsonCartEntityReader();
    Mockito.lenient()
        .when(mongoOperations.findOne(Query.query(where(QUERY_ID)
            .is(jsonCartEntityReader().getId())), CartEntity.class))
        .thenReturn(cartEntity);
    Assertions.assertDoesNotThrow(() -> itemUpdateHelper.validateItemId(
        jsonCartEntityReader().getId()));
  }

  public CartEntity jsonCartEntityReader() {
    InputStream in = ItemUpdateHelperTest.class.getResourceAsStream("/CartEntity.json");
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

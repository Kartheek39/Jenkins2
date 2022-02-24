package com.bt.marketplace.cart.util.helper;

import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.QUERY_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bt.marketplace.cart.entity.CartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class ItemUpdateHelper {

  @Autowired private MongoOperations mongoOperations;

  public CartEntity validateItemIdForCartId(String id, String cartItemId) {
    Query query = Query.query(
        where(QUERY_ID).is(id).and("cartItems.cartItemId").is(Long.parseLong(cartItemId)));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    return cartEntity;
  }
  public CartEntity validateItemId(String cartItemId) {
    Query query = Query.query(where(QUERY_ID).is(cartItemId));
    CartEntity cartEntity = mongoOperations.findOne(query, CartEntity.class);
    return cartEntity;
  }
}

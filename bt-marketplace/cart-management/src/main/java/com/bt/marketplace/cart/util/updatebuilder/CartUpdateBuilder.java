package com.bt.marketplace.cart.util.updatebuilder;


import com.bt.marketplace.cart.constants.queryfields.CartQueryFields;
import com.bt.marketplace.cart.entity.CartEntity;
import com.bt.marketplace.cart.entity.CartItem;
import com.bt.marketplace.common.querymanager.AbstractUpdate;
import com.bt.marketplace.common.querymanager.FieldUpdates;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Update builder for CartEntity (carts collection), provides simplified interface of update query
 * creation
 *
 * @author Dhananjay
 */
public class CartUpdateBuilder extends AbstractUpdate<CartUpdateBuilder> implements
    FieldUpdates<CartUpdateBuilder>, CartArrayUpdate<CartUpdateBuilder> {

  private String cartId;
  /**
   * set of array filter to avoid adding repatative filters in update
   */
  private Set<String> arrayFilterSet = new HashSet<>();

  /**
   * if constructed without id, then query needs to be manually set
   */
  public CartUpdateBuilder() {
    super(CartEntity.class);
  }

  /**
   * creates CartUpdateBuilder instance with default query for cartId passed.
   */
  public CartUpdateBuilder(String cartId) {
    super(CartEntity.class);
    this.cartId = cartId;
    this.query = query(where(CartQueryFields.ID).is(cartId));
  }

  /**
   * creates new update at the end of update queue if last update in queue is not empty, which will
   * be used for later update query creation. One use case for this is, in case of conflicting
   * operation on same path, ex: adding and removing packages, which happens one same path
   * "packages", can't be executed in same update. to handle such we can use this method to create
   * new instance of update.
   * <p>
   * array filter set clearing is also needed as arrayFilterSet will corresponsding to previous
   * update in queue and as we are starting new update, new filters will be needed for new update.
   *
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder startNewUpdate() {
    if (!this.isEmptyLastUpdate()) {
      this.getUpdateQueue().addLast(new Update());
      this.arrayFilterSet.clear();
    }
    return this;
  }

  /**
   * adds update operation to set the key to given value.
   *
   * @param key   : field key we want to update/add, will come from CartQueryFields
   * @param value : value which want to assign to field
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder setField(String key, Object value) {
    this.getUpdate().set(key, value);
    return this;
  }

  /**
   * adds update operation to set the key to given value.
   *
   * @param key   : field key we want to update/add, will come from CartQueryFields
   * @param value : value which want to assign to field
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder setIfNotEmpty(String key, Object value) {
    if (ObjectUtils.isNotEmpty(value)) {
      this.getUpdate().set(key, value);
    }
    return this;
  }

  /**
   * adds remove operation for field with given key
   *
   * @param key : field key we want to remove, will come from CartQueryFields
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder removeField(String key) {
    this.getUpdate().unset(key);
    return this;
  }

  /**
   * adds update operation to add new package in packages array
   *
   * @param item : new package that we want to add.
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder addItem(CartItem item) {
    this.getUpdate().push(CartQueryFields.CART_ITEMS, item);
    return this;
  }

  /**
   * adds update operation to remove package with given id from packages array
   *
   * @param cartItemId : id of package that we want to remove.
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder removeItem(Long cartItemId) {
    this.getUpdate().pull(CartQueryFields.CART_ITEMS,
        query(where(CartQueryFields.QUERY_FILTER_ITEM_ID).is(cartItemId)));
    return this;
  }

  /**
   * adds update operation to remove packages with given id from packages array
   *
   * @param cartItemIds : ids of packages that we want to remove.
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder removeItems(List<Long> cartItemIds) {
    this.getUpdate().pull(CartQueryFields.CART_ITEMS,
        query(where(CartQueryFields.QUERY_FILTER_ITEM_ID).in(cartItemIds)));
    return this;
  }

  /**
   * adds update operation to set particular field inside particular package in packages array
   *
   * @param cartItemId : id of package in which we want to update field
   * @param fieldKey  : field key which needs to be added or updated
   * @param value     : value that needs to be assigned to field
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder setItemField(Long cartItemId, String fieldKey, Object value) {
    setField(arrayField(fieldKey, cartItemId), value);
    addCartItemFilter(cartItemId);
    return this;
  }

  /**
   * adds update operation to remove particular field inside particular package in packages array
   *
   * @param cartItemId : id of package in which we want to remove field
   * @param fieldKey  : field key which needs to be removed
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder removeItemField(Long cartItemId, String fieldKey) {
    removeField(arrayField(fieldKey, cartItemId));
    addCartItemFilter(cartItemId);
    return this;
  }


  private void addCartItemFilter(Long cartItemId) {
    addArrayFilter(arrayFilter(CartQueryFields.ARRAY_FILTER_CART_ITEMS, cartItemId), cartItemId);
  }

  private void addArrayFilter(String key, Object value) {
    if (!arrayFilterSet.contains(key)) {
      this.getUpdate().filterArray(key, value);
      arrayFilterSet.add(key);
    }
  }

  private String arrayField(String arrayFieldKey, Object... arg) {
    processArgumentsForMessageFormatter(arg);
    return MessageFormat.format(arrayFieldKey, arg);
  }

  private String arrayFilter(String arrayFilterKey, Object... arg) {
    processArgumentsForMessageFormatter(arg);
    return MessageFormat.format(arrayFilterKey, arg);
  }


  private void processArgumentsForMessageFormatter(Object[] arg) {
    for (int i = 0; i < arg.length; i++) {
      if (arg[i] instanceof Long) {
        arg[i] = Long.toString((Long) arg[i]);
      }
    }
  }

  /**
   * @return :current query object for this update, used later in identifying the document for
   * update. if this is not set currently and cartId is present then default query will be returned
   * using that cartId, else null will be returned.
   */
  @Override
  public Query getQuery() {
    if (this.query != null) {
      return this.query;
    }
    return cartId != null ? query(where(CartQueryFields.CART_ID).is(cartId)) : null;
  }

  /**
   * CartUpdateBuilder is not constructed using id at the start the default query needs to be
   * added.
   *
   * @param query : query which we want to set for this update.
   * @return : current CartUpdateBuilder instance current CartUpdateBuilder instance
   */
  @Override
  public CartUpdateBuilder setQuery(Query query) {
    this.query = query;
    return this;
  }

  @Override
  public String getLastUpdatedAtFieldKey() {
    return CartQueryFields.LAST_UPDATED;
  }

  @Override
  public String getLastUpdatedByFieldKey() {
    return CartQueryFields.LAST_UPDATED_BY;
  }
}

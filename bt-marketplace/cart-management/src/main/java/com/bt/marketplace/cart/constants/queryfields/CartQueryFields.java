package com.bt.marketplace.cart.constants.queryfields;

public class CartQueryFields {

  private CartQueryFields() {

  }

  // CartEntity fields start
  public static final String ID = "id";
  public static final String QUERY_ID = "_id";
  public static final String EMPTY_UPDATE = "{}";
  public static final String CART_ID = "cartId";
  public static final String CART_ITEMS = "cartItems";
  public static final String ARRAY_FILTER_CART_ITEMS = "cartItems{0}.cartItemId";
  public static final String CART_ITEM_QUANTITY ="cartItems.$[cartItems{0}].quantity";
  public static final String CART_ITEM_OFFER_PRICE ="cartItems.$[cartItems{0}].offerPrice";
  public static final String CART_ITEM_TAX ="cartItems.$[cartItems{0}].tax";
  public static final String CART_ITEM_SUBTOTAL ="cartItems.$[cartItems{0}].subTotal";
  public static final String CART_ITEM_TOTAL ="cartItems.$[cartItems{0}].total";

  public static final String QUERY_FILTER_ITEM_ID = "cartItemId";
  public static final String LAST_UPDATED = "cartAudit.updatedTime";
  public static final String LAST_UPDATED_BY = "cartAudit.updatedBy";
  public static final String CART_STATUS = "status";
  public static final String CART_AUDIT_SUBMITTED_TIME = "cartAudit.submittedTime";
  public static final String CART_AUDIT_SUBMITTED_BY = "cartAudit.submittedBy";
  public static final String SHIPPING_METHOD = "shipping.selectedMethod";
  public static final String CART_ATTRIBUTE_NAME = "attributes.name";
  public static final String CART_ATTRIBUTE_CHECKOUT_REQUIRED = "attributes.checkoutRequired";
  public static final String CART_ATTRIBUTE_PAYMENT_AUTHORIZED = "attributes.paymentAuthorized";
  public static final String CART_CHANNEL = "attributes.channel";

  public static final String CART_TYPE ="type";

  public static final String BILLING_ADDRESS = "billingAddress";
  public static final String SHIPPING_ADDRESS = "shippingAddress";
  public static final String CART_PRICING = "cartPricing";

}

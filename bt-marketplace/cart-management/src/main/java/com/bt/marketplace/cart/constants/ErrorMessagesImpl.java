package com.bt.marketplace.cart.constants;

import com.bt.marketplace.common.exception.ApiError;
import com.bt.marketplace.common.exception.ErrorMessages;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessagesImpl implements ErrorMessages {


  public static final String INVALID_CART_ID = "INVALID_CART_ID";
  public static final String INVALID_ITEM_ID = "INVALID_ITEM_ID";
  public static final String FAILURE = "FAILURE";
  public static final String BAD_REQUEST_MESSAGE = "BAD_REQUEST";
  public static final String METHOD_ARGUEMENT_TYPE_MISMATCH = "Invalid arguement passed";
  public static final String PATH_VARIABLE_MISSING = "PATH_VARIABLE_MISSING";
  public static final String UNSUPPORTED_MEDIA_TYPE = "CART_UNSUPPORTED_MEDIA_TYPE";
  public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
  public static final String DATA_ACCESS_EXCEPTION = "DATA_ACCESS_EXCEPTION";
  public static final String INVALID_CHANNEL = "INVALID_CHANNEL";
  public static final String INVALID_ITEM_UPDATE_REQUEST = "INVALID_UPDATE_REQUEST";
  public static final String INVALID_CART_STATUS = "INVALID_CART_STATUS";
  public static final String MISSING_FIELD = "MISSING_FIELD";
  public static final String INVALID_SHIPPING_METHOD = "INVALID_SHIPPING_METHOD";
  public static final String INVALID_ITEM_QUANTITY = "INVALID_ITEM_QUANTITY";
  public static final String INVALID_SALES_PRICE = "INVALID_SALES_PRICE";

  //  create cart errors
  public static final String INVALID_CART_NAME = "INVALID_CART_NAME";
  public static final String INVALID_CART_TYPE = "INVALID_CART_TYPE";
  public static final String INVALID_CART_CHANNEL = "INVALID_CART_CHANNEL";

  // Add Item errors
  public static final String INVALID_ITEM_PRODUCT_ID = "INVALID_ITEM_PRODUCT_ID";
  public static final String INVALID_ITEM_SKU = "INVALID_ITEM_SKU";
  public static final String INVALID_ITEM_NAME = "INVALID_ITEM_NAME";

  private static final Map<String, String> systemErrorsMap = new HashMap<>();
  private static final Map<String, String> userErrorMap = new HashMap<>();

  static {
    systemErrorsMap.put(INVALID_CART_ID, "Cart Id is invalid");
    systemErrorsMap.put(INVALID_ITEM_ID, "Cart Item Id is invalid");
    systemErrorsMap.put(INVALID_ITEM_UPDATE_REQUEST, "Item Update Request is invalid");
    systemErrorsMap.put(INVALID_CHANNEL, "Channel is invalid");
    systemErrorsMap.put(INVALID_CART_STATUS, "Cart status is invalid to proceed with operation.");
    systemErrorsMap.put(INVALID_ITEM_QUANTITY, "Item quantity should be positive");
    systemErrorsMap.put(INVALID_SALES_PRICE, "Item sales price should be positive");
    systemErrorsMap.put(INVALID_SHIPPING_METHOD, "Shipping Method is invalid");

    systemErrorsMap.put(INVALID_CART_NAME, "Cart Name is invalid");
    systemErrorsMap.put(INVALID_CART_TYPE, "Cart Type is invalid");
    systemErrorsMap.put(INVALID_CART_CHANNEL, "Cart Channel is  invalid");
    systemErrorsMap.put(INVALID_ITEM_PRODUCT_ID, "Item Product Id is invalid");
    systemErrorsMap.put(INVALID_ITEM_SKU, "Item Product Sku is invalid");
    systemErrorsMap.put(INVALID_ITEM_NAME, "Item Product Name is  invalid");
  }

  public static String GENERIC_USER_MESSAGE = "We experienced a technical difficulty while processing your request. Please try again later.";

  static {
    userErrorMap.put(INVALID_ITEM_UPDATE_REQUEST, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_CART_ID, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_ITEM_ID, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_ITEM_UPDATE_REQUEST, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_CHANNEL, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_CART_STATUS, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_ITEM_QUANTITY, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_ITEM_UPDATE_REQUEST, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_SHIPPING_METHOD, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_SALES_PRICE, GENERIC_USER_MESSAGE);

    userErrorMap.put(INVALID_CART_NAME, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_CART_TYPE, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_CART_CHANNEL, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_ITEM_PRODUCT_ID, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_ITEM_SKU, GENERIC_USER_MESSAGE);
    userErrorMap.put(INVALID_ITEM_NAME, GENERIC_USER_MESSAGE);
  }

  @Override
  public ApiError getApiErrorByKey(String key) {
    return ApiError.newInstance(key, systemErrorsMap.getOrDefault(key, ""),
        userErrorMap.getOrDefault(key, ""));
  }

  @Override
  public ApiError getApiErrorByKeyAndArg(String key, Object arg1) {
    return ApiError.newInstance(key,
        MessageFormat.format(systemErrorsMap.getOrDefault(key, ""), arg1),
        userErrorMap.getOrDefault(key, ""));
  }

  public static ApiError getApiError(String key) {
    return ApiError.newInstance(key, systemErrorsMap.getOrDefault(key, ""),
        userErrorMap.getOrDefault(key, ""));
  }

  public static ApiError getApiError(String key, Object arg1) {
    return ApiError.newInstance(key,
        MessageFormat.format(systemErrorsMap.getOrDefault(key, ""), arg1),
        userErrorMap.getOrDefault(key, ""));
  }

  public static ApiError getApiError(String key, Object... args) {
    return ApiError.newInstance(key,
        MessageFormat.format(systemErrorsMap.getOrDefault(key, ""), args),
        userErrorMap.getOrDefault(key, ""));
  }

  public static ApiError getApiErrorWithKeyFormatting(Object arg, String key, Object... args) {
    return ApiError.newInstance(MessageFormat.format(key, arg),
        MessageFormat.format(systemErrorsMap.getOrDefault(key, ""), args),
        userErrorMap.getOrDefault(key, ""));
  }

}

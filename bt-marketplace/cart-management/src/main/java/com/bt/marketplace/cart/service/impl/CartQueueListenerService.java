package com.bt.marketplace.cart.service.impl;

import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.CART_ATTRIBUTE_CHECKOUT_REQUIRED;
import static com.bt.marketplace.cart.constants.queryfields.CartQueryFields.CART_STATUS;

import com.bt.marketplace.cart.util.updatebuilder.CartUpdateBuilder;
import com.bt.marketplace.common.constants.CartStatus;
import com.bt.marketplace.common.constants.MessagingConstants;
import com.bt.marketplace.common.exception.ApiErrorResponseException;
import com.bt.marketplace.common.model.messaging.checkout.GenericEvent;
import com.bt.marketplace.common.querymanager.UpdateQueryManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CartQueueListenerService {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  ObjectMapper objectMapper;

  @SqsListener(deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS,
      value = MessagingConstants.CART_EVENTS_QUEUE)
  public void queueListener(String genericEventString) throws JsonProcessingException {
    GenericEvent genericEvent;
    if (genericEventString.contains("Notification")) {
      genericEvent = objectMapper.readValue(objectMapper.readValue(genericEventString, Map.class).get("Message").toString()
          , GenericEvent.class);
    } else {
      genericEvent = objectMapper.readValue(genericEventString, GenericEvent.class);
    }

    UpdateQueryManager<CartUpdateBuilder> updateQueryManager =
        applicationContext.getBean(UpdateQueryManager.class);


    if (MessagingConstants.CART_CALCULATED_EVENT.equals(genericEvent.getEventName())) {
      updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, genericEvent.getCartId())
          .setField(CART_ATTRIBUTE_CHECKOUT_REQUIRED, false);
    } else if (MessagingConstants.ORDER_SUBMITTED_EVENT.equals(genericEvent.getEventName())) {
      updateQueryManager.getUpdateBuilder(CartUpdateBuilder.class, genericEvent.getCartId())
          .setField(CART_STATUS, CartStatus.SUBMITTED.name());
    }
    updateQueryManager.executeUpdates();
  }


}

package com.bt.marketplace.cart.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

import com.bt.marketplace.cart.util.updatebuilder.CartUpdateBuilder;
import com.bt.marketplace.common.model.messaging.checkout.GenericEvent;
import com.bt.marketplace.common.querymanager.UpdateQueryManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
public class CartQueueListenerServiceTest {

  @InjectMocks
  private CartQueueListenerService cartQueueListenerService;

  @Mock
  ApplicationContext applicationContext;

  @Mock
  private UpdateQueryManager<CartUpdateBuilder> updateQueryManager;

  @Test
  void queueListenerTest(){
    GenericEvent genericEvent = GenericEvent.builder()
        .eventName("CartCalculated")
        .build();
    CartUpdateBuilder cartCheckOutUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartCheckOutUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    assertDoesNotThrow(() -> cartQueueListenerService.queueListener("Notification"));
  }

  @Test
  void queueListenerOrderSubmittedTest() {
    GenericEvent genericEvent = GenericEvent.builder()
        .eventName("OrderSubmitted")
        .build();
    CartUpdateBuilder cartCheckOutUpdateBuilder = new CartUpdateBuilder();
    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
        .thenReturn(cartCheckOutUpdateBuilder);
    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
        .thenReturn(updateQueryManager);
    assertDoesNotThrow(() -> cartQueueListenerService.queueListener("Notification"));
  }
//  @Test
//  void notificationListenerTest(){
//    GenericEvent genericEvent = GenericEvent.builder()
//        .eventName("OrderSubmitted")
//        .build();
//    CartUpdateBuilder cartCheckOutUpdateBuilder = new CartUpdateBuilder();
//    Mockito.lenient().when(updateQueryManager.getUpdateBuilder(any(), any()))
//        .thenReturn(cartCheckOutUpdateBuilder);
//    Mockito.lenient().when(applicationContext.getBean(UpdateQueryManager.class))
//        .thenReturn(updateQueryManager);
//    assertDoesNotThrow(() -> cartQueueListenerService.notificationListener(genericEvent));
//  }
}

package com.stockbrokerage.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendOrderNotification(Long userId, Object message) {
        messagingTemplate.convertAndSend("/topic/orders/" + userId, message);
    }

    public void sendPriceUpdate(String symbol, Object message) {
        messagingTemplate.convertAndSend("/topic/prices/" + symbol, message);
    }

    public void sendTransactionNotification(Long userId, Object message) {
        messagingTemplate.convertAndSend("/topic/transactions/" + userId, message);
    }
}


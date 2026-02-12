package com.stockbrokerage.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockbrokerage.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitMQListener {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "order.queue")
    public void handleOrderEvent(String message) {
        try {
            Map<String, Object> order = objectMapper.readValue(message, Map.class);
            Long userId = Long.valueOf(order.get("userId").toString());
            notificationService.sendOrderNotification(userId, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "price.queue")
    public void handlePriceUpdate(String message) {
        try {
            Map<String, Object> priceUpdate = objectMapper.readValue(message, Map.class);
            String symbol = priceUpdate.get("symbol").toString();
            notificationService.sendPriceUpdate(symbol, priceUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "transaction.queue")
    public void handleTransactionEvent(String message) {
        try {
            Map<String, Object> transaction = objectMapper.readValue(message, Map.class);
            // Extract userId from transaction and send notification
            // This would need to be enhanced based on transaction structure
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


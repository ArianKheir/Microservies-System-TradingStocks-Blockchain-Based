package com.stockbrokerage.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockbrokerage.order.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ORDER_EXCHANGE = "order.exchange";
    private static final String ORDER_ROUTING_KEY = "order.status";

    public void publishOrderEvent(Order order) {
        try {
            String message = objectMapper.writeValueAsString(order);
            rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


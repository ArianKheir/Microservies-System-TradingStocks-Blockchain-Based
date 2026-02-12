package com.stockbrokerage.stock.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String PRICE_EXCHANGE = "price.exchange";
    private static final String PRICE_ROUTING_KEY = "price.update";

    public void publishPriceUpdate(String symbol, BigDecimal price) {
        String message = String.format("{\"symbol\":\"%s\",\"price\":%s}", symbol, price);
        rabbitTemplate.convertAndSend(PRICE_EXCHANGE, PRICE_ROUTING_KEY, message);
    }
}


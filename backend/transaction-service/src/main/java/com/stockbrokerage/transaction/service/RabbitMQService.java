package com.stockbrokerage.transaction.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockbrokerage.transaction.model.BlockchainTransaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TRANSACTION_EXCHANGE = "transaction.exchange";
    private static final String TRANSACTION_ROUTING_KEY = "transaction.recorded";

    public void publishTransactionEvent(BlockchainTransaction transaction) {
        try {
            String message = objectMapper.writeValueAsString(transaction);
            rabbitTemplate.convertAndSend(TRANSACTION_EXCHANGE, TRANSACTION_ROUTING_KEY, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package com.stockbrokerage.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_ROUTING_KEY = "order.status";

    public static final String PRICE_EXCHANGE = "price.exchange";
    public static final String PRICE_QUEUE = "price.queue";
    public static final String PRICE_ROUTING_KEY = "price.update";

    public static final String TRANSACTION_EXCHANGE = "transaction.exchange";
    public static final String TRANSACTION_QUEUE = "transaction.queue";
    public static final String TRANSACTION_ROUTING_KEY = "transaction.recorded";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, false);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder
            .bind(orderQueue())
            .to(orderExchange())
            .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public TopicExchange priceExchange() {
        return new TopicExchange(PRICE_EXCHANGE);
    }

    @Bean
    public Queue priceQueue() {
        return new Queue(PRICE_QUEUE, false);
    }

    @Bean
    public Binding priceBinding() {
        return BindingBuilder
            .bind(priceQueue())
            .to(priceExchange())
            .with(PRICE_ROUTING_KEY);
    }

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(TRANSACTION_EXCHANGE);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE, false);
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder
            .bind(transactionQueue())
            .to(transactionExchange())
            .with(TRANSACTION_ROUTING_KEY);
    }
}


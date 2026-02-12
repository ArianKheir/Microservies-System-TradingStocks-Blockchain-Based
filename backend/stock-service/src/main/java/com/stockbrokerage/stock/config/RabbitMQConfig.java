package com.stockbrokerage.stock.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PRICE_EXCHANGE = "price.exchange";
    public static final String PRICE_QUEUE = "price.queue";
    public static final String PRICE_ROUTING_KEY = "price.update";

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
}


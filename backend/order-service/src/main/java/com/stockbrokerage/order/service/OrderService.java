package com.stockbrokerage.order.service;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.model.OrderStatus;
import com.stockbrokerage.order.model.Trade;
import com.stockbrokerage.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMatchingEngine matchingEngine;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        order.setFilledQuantity(0L);
        Order savedOrder = orderRepository.save(order);

        // Try to match the order
        List<Trade> trades = matchingEngine.matchOrder(savedOrder);
        
        // Update order status based on matching results
        if (!trades.isEmpty()) {
            savedOrder = orderRepository.findById(savedOrder.getId()).orElse(savedOrder);
            if (savedOrder.getFilledQuantity() >= savedOrder.getQuantity()) {
                savedOrder.setStatus(OrderStatus.COMPLETED);
            } else {
                savedOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
            }
            savedOrder = orderRepository.save(savedOrder);
        }

        // Publish order event
        rabbitMQService.publishOrderEvent(savedOrder);

        return savedOrder;
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public boolean cancelOrder(Long orderId, Long userId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (!order.getUserId().equals(userId)) {
                return false;
            }
            if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PARTIALLY_FILLED) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                rabbitMQService.publishOrderEvent(order);
                return true;
            }
        }
        return false;
    }
}


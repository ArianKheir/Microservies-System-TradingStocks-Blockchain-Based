package com.stockbrokerage.order.service;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.model.OrderStatus;
import com.stockbrokerage.order.model.OrderType;
import com.stockbrokerage.order.model.Trade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderMatchingEngine {
    private final ConcurrentHashMap<String, PriorityQueue<Order>> buyOrders = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PriorityQueue<Order>> sellOrders = new ConcurrentHashMap<>();

    public synchronized List<Trade> matchOrder(Order newOrder) {
        List<Trade> executedTrades = new ArrayList<>();
        String symbol = newOrder.getStockSymbol();

        if (newOrder.getOrderType() == OrderType.BUY) {
            addToQueue(buyOrders, symbol, newOrder);
            executedTrades.addAll(matchBuyWithSell(symbol));
        } else {
            addToQueue(sellOrders, symbol, newOrder);
            executedTrades.addAll(matchSellWithBuy(symbol));
        }

        return executedTrades;
    }

    private void addToQueue(ConcurrentHashMap<String, PriorityQueue<Order>> orderMap, String symbol, Order order) {
        orderMap.computeIfAbsent(symbol, k -> new PriorityQueue<>(
            Comparator.comparing(Order::getPrice)
                .thenComparing(Order::getCreatedAt)
        )).add(order);
    }

    private List<Trade> matchBuyWithSell(String symbol) {
        List<Trade> trades = new ArrayList<>();
        PriorityQueue<Order> buyQueue = buyOrders.get(symbol);
        PriorityQueue<Order> sellQueue = sellOrders.get(symbol);

        if (buyQueue == null || sellQueue == null || buyQueue.isEmpty() || sellQueue.isEmpty()) {
            return trades;
        }

        while (!buyQueue.isEmpty() && !sellQueue.isEmpty()) {
            Order buyOrder = buyQueue.peek();
            Order sellOrder = sellQueue.peek();

            if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) < 0) {
                break; // No match possible
            }

            buyQueue.poll();
            sellQueue.poll();

            long matchQuantity = Math.min(
                buyOrder.getQuantity() - buyOrder.getFilledQuantity(),
                sellOrder.getQuantity() - sellOrder.getFilledQuantity()
            );
            BigDecimal matchPrice = sellOrder.getPrice(); // Price priority to seller

            Trade trade = new Trade();
            trade.setBuyOrderId(buyOrder.getId());
            trade.setSellOrderId(sellOrder.getId());
            trade.setQuantity(matchQuantity);
            trade.setPrice(matchPrice);
            trade.setStockSymbol(symbol);
            trade.setTimestamp(LocalDateTime.now());
            trades.add(trade);

            // Update filled quantities
            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + matchQuantity);
            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + matchQuantity);

            // Update status
            if (buyOrder.getFilledQuantity() >= buyOrder.getQuantity()) {
                buyOrder.setStatus(OrderStatus.COMPLETED);
            } else {
                buyOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
                buyQueue.add(buyOrder);
            }

            if (sellOrder.getFilledQuantity() >= sellOrder.getQuantity()) {
                sellOrder.setStatus(OrderStatus.COMPLETED);
            } else {
                sellOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
                sellQueue.add(sellOrder);
            }
        }

        return trades;
    }

    private List<Trade> matchSellWithBuy(String symbol) {
        // Similar logic but reversed
        return matchBuyWithSell(symbol);
    }
}


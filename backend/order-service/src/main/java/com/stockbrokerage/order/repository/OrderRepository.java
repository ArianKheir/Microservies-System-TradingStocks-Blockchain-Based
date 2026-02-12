package com.stockbrokerage.order.repository;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.stockSymbol = :symbol AND o.status IN :statuses ORDER BY " +
           "CASE WHEN o.orderType = 'BUY' THEN o.price END DESC, " +
           "CASE WHEN o.orderType = 'SELL' THEN o.price END ASC, " +
           "o.createdAt ASC")
    List<Order> findActiveOrdersBySymbol(@Param("symbol") String symbol, @Param("statuses") List<OrderStatus> statuses);
}


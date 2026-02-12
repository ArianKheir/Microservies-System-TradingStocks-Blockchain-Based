package com.stockbrokerage.stock.repository;

import com.stockbrokerage.stock.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByStockIdOrderByTimestampDesc(Long stockId);
    
    @Query("SELECT ph FROM PriceHistory ph WHERE ph.stockId = :stockId AND ph.timestamp >= :fromDate ORDER BY ph.timestamp ASC")
    List<PriceHistory> findByStockIdAndTimestampAfter(@Param("stockId") Long stockId, @Param("fromDate") LocalDateTime fromDate);
}


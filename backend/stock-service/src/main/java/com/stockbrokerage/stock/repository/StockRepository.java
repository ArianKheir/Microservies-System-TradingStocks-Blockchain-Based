package com.stockbrokerage.stock.repository;

import com.stockbrokerage.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySymbol(String symbol);
    
    @Query("SELECT s FROM Stock s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:symbol IS NULL OR LOWER(s.symbol) LIKE LOWER(CONCAT('%', :symbol, '%')))")
    List<Stock> searchStocks(@Param("name") String name, @Param("symbol") String symbol);
}


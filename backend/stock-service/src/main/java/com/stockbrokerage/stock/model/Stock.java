package com.stockbrokerage.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(name = "current_price", nullable = false, precision = 20, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "total_supply")
    private Long totalSupply;

    @Column(name = "market_cap", precision = 30, scale = 2)
    private BigDecimal marketCap;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}


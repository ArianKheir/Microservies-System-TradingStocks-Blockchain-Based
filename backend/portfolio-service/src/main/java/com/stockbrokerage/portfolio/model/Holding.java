package com.stockbrokerage.portfolio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "holdings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"portfolio_id", "stock_symbol"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false)
    private Long portfolioId;

    @Column(name = "stock_symbol", nullable = false, length = 10)
    private String stockSymbol;

    @Column(nullable = false)
    private Long quantity;

    @Column(name = "average_buy_price", precision = 20, scale = 2)
    private BigDecimal averageBuyPrice;
}


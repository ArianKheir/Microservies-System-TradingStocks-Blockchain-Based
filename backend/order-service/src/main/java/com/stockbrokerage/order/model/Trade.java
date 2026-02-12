package com.stockbrokerage.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private Long buyOrderId;
    private Long sellOrderId;
    private Long quantity;
    private BigDecimal price;
    private String stockSymbol;
    private LocalDateTime timestamp;
}


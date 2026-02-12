package com.stockbrokerage.transaction.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "blockchain_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_hash", unique = true, nullable = false, length = 66)
    private String txHash;

    @Column(name = "from_address", nullable = false, length = 42)
    private String fromAddress;

    @Column(name = "to_address", nullable = false, length = 42)
    private String toAddress;

    @Column(name = "stock_symbol", nullable = false, length = 10)
    private String stockSymbol;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal price;

    @Column(name = "block_number")
    private Long blockNumber;

    @Column
    private LocalDateTime timestamp;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}


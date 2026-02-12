package com.stockbrokerage.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "user_wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(precision = 20, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(length = 10)
    private String currency = "USD";
}


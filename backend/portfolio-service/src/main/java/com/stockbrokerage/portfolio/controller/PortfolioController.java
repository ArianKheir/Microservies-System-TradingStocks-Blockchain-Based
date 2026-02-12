package com.stockbrokerage.portfolio.controller;

import com.stockbrokerage.portfolio.model.Holding;
import com.stockbrokerage.portfolio.model.Portfolio;
import com.stockbrokerage.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{userId}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long userId) {
        Portfolio portfolio = portfolioService.getOrCreatePortfolio(userId);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/{userId}/holdings")
    public ResponseEntity<List<Holding>> getHoldings(@PathVariable Long userId) {
        List<Holding> holdings = portfolioService.getHoldings(userId);
        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/{userId}/performance")
    public ResponseEntity<BigDecimal> getPerformance(@PathVariable Long userId) {
        BigDecimal totalValue = portfolioService.calculateTotalValue(userId);
        return ResponseEntity.ok(totalValue);
    }
}


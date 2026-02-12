package com.stockbrokerage.portfolio.service;

import com.stockbrokerage.portfolio.model.Holding;
import com.stockbrokerage.portfolio.model.Portfolio;
import com.stockbrokerage.portfolio.repository.HoldingRepository;
import com.stockbrokerage.portfolio.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    @Transactional
    public Portfolio getOrCreatePortfolio(Long userId) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findByUserId(userId);
        if (portfolioOpt.isPresent()) {
            return portfolioOpt.get();
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(userId);
        portfolio.setTotalValue(BigDecimal.ZERO);
        portfolio.setCreatedAt(LocalDateTime.now());
        return portfolioRepository.save(portfolio);
    }

    public List<Holding> getHoldings(Long userId) {
        Portfolio portfolio = getOrCreatePortfolio(userId);
        return holdingRepository.findByPortfolioId(portfolio.getId());
    }

    @Transactional
    public void updateHolding(Long userId, String stockSymbol, Long quantity, BigDecimal price) {
        Portfolio portfolio = getOrCreatePortfolio(userId);
        Optional<Holding> holdingOpt = holdingRepository.findByPortfolioIdAndStockSymbol(
            portfolio.getId(), stockSymbol
        );

        if (holdingOpt.isPresent()) {
            Holding holding = holdingOpt.get();
            long newQuantity = holding.getQuantity() + quantity;
            if (newQuantity <= 0) {
                holdingRepository.delete(holding);
            } else {
                // Recalculate average buy price
                BigDecimal totalCost = holding.getAverageBuyPrice()
                    .multiply(BigDecimal.valueOf(holding.getQuantity()))
                    .add(price.multiply(BigDecimal.valueOf(quantity)));
                BigDecimal newAvgPrice = totalCost.divide(BigDecimal.valueOf(newQuantity), 2, BigDecimal.ROUND_HALF_UP);
                
                holding.setQuantity(newQuantity);
                holding.setAverageBuyPrice(newAvgPrice);
                holdingRepository.save(holding);
            }
        } else if (quantity > 0) {
            Holding holding = new Holding();
            holding.setPortfolioId(portfolio.getId());
            holding.setStockSymbol(stockSymbol);
            holding.setQuantity(quantity);
            holding.setAverageBuyPrice(price);
            holdingRepository.save(holding);
        }
    }

    public BigDecimal calculateTotalValue(Long userId) {
        Portfolio portfolio = getOrCreatePortfolio(userId);
        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolio.getId());
        // This would typically fetch current prices from stock service
        // For now, return portfolio total value
        return portfolio.getTotalValue();
    }
}


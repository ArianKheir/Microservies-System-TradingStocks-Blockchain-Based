package com.stockbrokerage.stock.service;

import com.stockbrokerage.stock.model.PriceHistory;
import com.stockbrokerage.stock.model.Stock;
import com.stockbrokerage.stock.repository.PriceHistoryRepository;
import com.stockbrokerage.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private RabbitMQService rabbitMQService;

    private final Random random = new Random();

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public List<Stock> searchStocks(String name, String symbol) {
        return stockRepository.searchStocks(name, symbol);
    }

    public List<PriceHistory> getPriceHistory(String symbol, LocalDateTime fromDate) {
        Optional<Stock> stock = stockRepository.findBySymbol(symbol);
        if (stock.isEmpty()) {
            return List.of();
        }
        if (fromDate != null) {
            return priceHistoryRepository.findByStockIdAndTimestampAfter(stock.get().getId(), fromDate);
        }
        return priceHistoryRepository.findByStockIdOrderByTimestampDesc(stock.get().getId());
    }

    @Transactional
    public Stock createStock(Stock stock) {
        stock.setCreatedAt(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    @Transactional
    public void updateStockPrice(String symbol, BigDecimal newPrice) {
        Optional<Stock> stockOpt = stockRepository.findBySymbol(symbol);
        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            BigDecimal oldPrice = stock.getCurrentPrice();
            stock.setCurrentPrice(newPrice);
            stockRepository.save(stock);

            // Save price history
            PriceHistory history = new PriceHistory();
            history.setStockId(stock.getId());
            history.setPrice(newPrice);
            history.setTimestamp(LocalDateTime.now());
            priceHistoryRepository.save(history);

            // Publish price update to RabbitMQ
            rabbitMQService.publishPriceUpdate(symbol, newPrice);
        }
    }

    @Transactional
    public void simulatePriceUpdate() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            BigDecimal currentPrice = stock.getCurrentPrice();
            // Random price change between -2% and +2%
            double changePercent = (random.nextDouble() - 0.5) * 0.04;
            BigDecimal newPrice = currentPrice.multiply(BigDecimal.valueOf(1 + changePercent))
                .setScale(2, RoundingMode.HALF_UP);
            updateStockPrice(stock.getSymbol(), newPrice);
        }
    }
}


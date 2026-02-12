package com.stockbrokerage.stock.scheduler;

import com.stockbrokerage.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceUpdateScheduler {
    @Autowired
    private StockService stockService;

    @Scheduled(fixedRate = 5000) // Update every 5 seconds
    public void updatePrices() {
        stockService.simulatePriceUpdate();
    }
}


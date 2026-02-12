package com.stockbrokerage.stock.controller;

import com.stockbrokerage.stock.model.PriceHistory;
import com.stockbrokerage.stock.model.Stock;
import com.stockbrokerage.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String symbol) {
        if (name != null || symbol != null) {
            return ResponseEntity.ok(stockService.searchStocks(name, symbol));
        }
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable String symbol) {
        Optional<Stock> stock = stockService.getStockBySymbol(symbol);
        return stock.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{symbol}/history")
    public ResponseEntity<List<PriceHistory>> getPriceHistory(
            @PathVariable String symbol,
            @RequestParam(required = false) LocalDateTime fromDate) {
        List<PriceHistory> history = stockService.getPriceHistory(symbol, fromDate);
        return ResponseEntity.ok(history);
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        Stock created = stockService.createStock(stock);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{symbol}/price")
    public ResponseEntity<Void> updatePrice(
            @PathVariable String symbol,
            @RequestParam BigDecimal price) {
        stockService.updateStockPrice(symbol, price);
        return ResponseEntity.ok().build();
    }
}


package com.stockbrokerage.transaction.controller;

import com.stockbrokerage.transaction.model.BlockchainTransaction;
import com.stockbrokerage.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blockchain")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/record")
    public ResponseEntity<BlockchainTransaction> recordTransaction(
            @RequestParam String fromAddress,
            @RequestParam String toAddress,
            @RequestParam String stockSymbol,
            @RequestParam Long quantity,
            @RequestParam BigDecimal price) {
        try {
            BlockchainTransaction tx = transactionService.recordTransaction(
                fromAddress, toAddress, stockSymbol, quantity, price
            );
            return ResponseEntity.status(201).body(tx);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/transaction/{txHash}")
    public ResponseEntity<BlockchainTransaction> getTransaction(@PathVariable String txHash) {
        Optional<BlockchainTransaction> tx = transactionService.getTransactionByHash(txHash);
        return tx.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/verify/{txHash}")
    public ResponseEntity<Boolean> verifyTransaction(@PathVariable String txHash) {
        boolean verified = transactionService.verifyTransaction(txHash);
        return ResponseEntity.ok(verified);
    }

    @GetMapping("/user/{address}/transactions")
    public ResponseEntity<List<BlockchainTransaction>> getUserTransactions(@PathVariable String address) {
        List<BlockchainTransaction> transactions = transactionService.getUserTransactions(address);
        return ResponseEntity.ok(transactions);
    }
}


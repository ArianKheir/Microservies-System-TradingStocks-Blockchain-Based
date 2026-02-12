package com.stockbrokerage.transaction.service;

import com.stockbrokerage.transaction.model.BlockchainTransaction;
import com.stockbrokerage.transaction.model.TransactionStatus;
import com.stockbrokerage.transaction.repository.BlockchainTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private BlockchainTransactionRepository repository;

    @Autowired
    private Web3Service web3Service;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Transactional
    public BlockchainTransaction recordTransaction(String fromAddress, String toAddress,
                                                   String stockSymbol, Long quantity,
                                                   BigDecimal price) {
        try {
            String txHash = web3Service.recordTradeOnBlockchain(
                fromAddress, toAddress, stockSymbol, quantity, price
            );

            BlockchainTransaction transaction = new BlockchainTransaction();
            transaction.setTxHash(txHash);
            transaction.setFromAddress(fromAddress);
            transaction.setToAddress(toAddress);
            transaction.setStockSymbol(stockSymbol);
            transaction.setQuantity(quantity);
            transaction.setPrice(price);
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setTimestamp(LocalDateTime.now());

            BlockchainTransaction saved = repository.save(transaction);

            // Publish transaction event
            rabbitMQService.publishTransactionEvent(saved);

            return saved;
        } catch (Exception e) {
            throw new RuntimeException("Failed to record transaction on blockchain", e);
        }
    }

    public Optional<BlockchainTransaction> getTransactionByHash(String txHash) {
        return repository.findByTxHash(txHash);
    }

    public boolean verifyTransaction(String txHash) {
        boolean verified = web3Service.verifyTransaction(txHash);
        if (verified) {
            Optional<BlockchainTransaction> txOpt = repository.findByTxHash(txHash);
            if (txOpt.isPresent()) {
                BlockchainTransaction tx = txOpt.get();
                tx.setStatus(TransactionStatus.CONFIRMED);
                repository.save(tx);
            }
        }
        return verified;
    }

    public List<BlockchainTransaction> getUserTransactions(String address) {
        return repository.findByFromAddressOrToAddress(address, address);
    }
}


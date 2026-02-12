package com.stockbrokerage.transaction.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class Web3Service {
    private final Web3j web3j;

    @Value("${web3j.node-url:http://localhost:8545}")
    private String nodeUrl;

    public Web3Service(@Value("${web3j.node-url:http://localhost:8545}") String nodeUrl) {
        this.nodeUrl = nodeUrl;
        this.web3j = Web3j.build(new HttpService(nodeUrl));
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    public String recordTradeOnBlockchain(String buyerAddress, String sellerAddress, 
                                         String stockSymbol, Long quantity, 
                                         BigDecimal price) throws Exception {
        // This is a simplified version - in production, you would interact with deployed smart contracts
        // For now, we'll simulate a transaction hash
        String txHash = "0x" + java.util.UUID.randomUUID().toString().replace("-", "");
        
        // In production, you would:
        // 1. Load the TradeRegistry contract
        // 2. Call recordTrade function
        // 3. Wait for transaction receipt
        // 4. Return the actual transaction hash
        
        return txHash;
    }

    public boolean verifyTransaction(String txHash) {
        try {
            org.web3j.protocol.core.methods.response.EthTransaction tx = 
                web3j.ethGetTransactionByHash(txHash).send();
            return tx.getTransaction().isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}


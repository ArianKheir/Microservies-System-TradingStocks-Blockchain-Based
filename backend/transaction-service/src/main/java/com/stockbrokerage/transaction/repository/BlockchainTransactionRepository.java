package com.stockbrokerage.transaction.repository;

import com.stockbrokerage.transaction.model.BlockchainTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockchainTransactionRepository extends JpaRepository<BlockchainTransaction, Long> {
    Optional<BlockchainTransaction> findByTxHash(String txHash);
    
    List<BlockchainTransaction> findByFromAddressOrToAddress(String fromAddress, String toAddress);
}


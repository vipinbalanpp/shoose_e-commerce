package com.vipin.shoose.repository;

import com.vipin.shoose.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions,Long> {
    List<Transactions> findByWallet_WalletId(Long walletId);
}

package com.vipin.shoose.repository;

import com.vipin.shoose.model.User;
import com.vipin.shoose.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Wallet findByUser(User user);

    boolean existsByUser(User user);
}

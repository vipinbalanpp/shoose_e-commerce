package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long walletId;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    private Double balance;
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}

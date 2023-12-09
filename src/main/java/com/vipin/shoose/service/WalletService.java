package com.vipin.shoose.service;

import com.vipin.shoose.model.User;

import java.math.BigDecimal;

public interface WalletService {
    void createWallet(User user);

    void addWelcomeBonus( User user);


    void addReferralBonus(User user);


}

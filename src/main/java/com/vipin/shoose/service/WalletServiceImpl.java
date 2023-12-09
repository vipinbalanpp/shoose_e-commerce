package com.vipin.shoose.service;

import com.vipin.shoose.model.*;
import com.vipin.shoose.repository.TransactionRepository;
import com.vipin.shoose.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletServiceImpl implements WalletService{
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void createWallet(User user) {
        Wallet wallet=new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.00);
        walletRepository.save(wallet);
    }

    @Override
    public void addWelcomeBonus( User user) {
        try{
            Wallet userWallet=walletRepository.findByUser(user);
            userWallet.setBalance(userWallet.getBalance()+10.00);
            walletRepository.save(userWallet);
            Transactions welcomeBonus=new Transactions();
            welcomeBonus.setAmount(10.00);
            welcomeBonus.setWallet(userWallet);
            welcomeBonus.setTransactionStatus(TransactionStatus.CREDIT);
            welcomeBonus.setTransactionTime(LocalDateTime.now());
            welcomeBonus.setTransactionType(TransactionType.WELCOME_BONUS);
            transactionRepository.save(welcomeBonus);
        }catch (Exception e){
            throw  new RuntimeException("error from walletService");
        }

    }

    @Override
    public void addReferralBonus(User user) {
        try{
            Wallet userWallet=walletRepository.findByUser(user);
            userWallet.setBalance(userWallet.getBalance()+20.00);
            walletRepository.save(userWallet);
            Transactions referralBonus=new Transactions();
            referralBonus.setAmount(10.00);
            referralBonus.setWallet(userWallet);
            referralBonus.setTransactionStatus(TransactionStatus.CREDIT);
            referralBonus.setTransactionType(TransactionType.REFERRAL_BONUS);
            referralBonus.setTransactionTime(LocalDateTime.now());
            transactionRepository.save(referralBonus);
        }catch (Exception e){
            throw  new RuntimeException("error from walletService");
        }

    }


}

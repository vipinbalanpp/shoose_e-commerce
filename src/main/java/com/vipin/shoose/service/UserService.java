package com.vipin.shoose.service;

import com.vipin.shoose.dto.TransactionDto;
import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.model.Address;
import com.vipin.shoose.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    public boolean verifyOtp(UserDto userDto, String otp);

    void blockUser(Long id);

    void unBlockUser(Long id);

    void sendOtp(UserDto userDto);

    void setNewPassword(UserDto userDto);

    void resendMail(UserDto userDto);

    void changeUsername(User user, String newUsername);

    User getCurrentUser();


    boolean  checkCurrentPassword(User user, String oldPassword);

    void changePassword(User user, String newPassword);
    void  updateSecurityContext(User user,String newPassword);

    List<Address> getAddresses( User user);

    int getTotalUsers();


    Boolean verifyReferralId(String referralId);

    Double getWalletBalance();

    void payfromWallet(Double totalAmount);

    void cancelOrderMoneyReturn(Double amountPayed);


    void addMoneytoWallet(Double amount);

    List<TransactionDto> getAllWalletTransactions();
}

package com.vipin.shoose.service;
import com.vipin.shoose.configuration.CustomUserDetailsService;
import com.vipin.shoose.dto.TransactionDto;
import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.exception.OtpInvalidException;
import com.vipin.shoose.model.*;
import com.vipin.shoose.repository.*;
import com.vipin.shoose.util.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    SessionRegistry sessionRegistry;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    WalletService walletService;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OtpService otpService;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionRepository transactionRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 6;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean verifyOtp(UserDto userDto, String otp) {
        User user=new User();
        if(userDto!=null&&otpService.isOtpValid(userDto,otp)){
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setRoles("USER");
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEnabled(true);
            System.out.println("before");
            user.setReferralId(createReferalId(userDto.getUsername()));
            System.out.println("after");
            user.setCreatedTime(LocalDateTime.now());
            userRepository.save(user);
            Cart cart=new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
            walletService.createWallet(user);
            if(!Objects.equals(userDto.getReferalId(), "")&&userDto.getReferalId()!=null){
                walletService.addWelcomeBonus(user);
                walletService.addReferralBonus(userRepository.findByReferralId(userDto.getReferalId()));
            }
           return true;
        } else {
            throw  new OtpInvalidException("Invalid Otp");

        }
    }


    public String createReferalId(String username) {
        System.out.println("referalId");
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        sb.append(username);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        System.out.println(sb);

        return sb.toString();
    }

    @Override
    public void blockUser(Long id) {
        User user=userRepository.findByUserId(id);
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        System.out.println("fdslfnsdlfjsdlfsndfl");
        for (Object principal : allPrincipals) {
            if (principal instanceof UserDetails otherUserDetails) {
                if (otherUserDetails.getUsername().equals(user.getUsername())) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        System.out.println(session);
                        session.expireNow();
                    }
                }
            }
        }
        user.setEnabled(false);
  userRepository.save(user) ;


    }

    @Override
    public void unBlockUser(Long id) {
        User user=userRepository.findByUserId(id);
        user.setEnabled(true);
        userRepository.save(user) ;
    }

    @Override
    public void sendOtp(UserDto userDto) {
        String otp=otpService.generateOtp();
        userDto.setOtp(otp);
        userDto.setOtpGeneratedTime(LocalDateTime.now());
        otpService.sendOTPEmail(userDto.getEmail(),otp);
    }

    @Override
    public void setNewPassword(UserDto userDto) {
        try {
            User user=userRepository.findByEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException("caught error");
        }
    }

    @Override
    public void resendMail(UserDto userDto) {
        userDto.setOtp(otpService.generateOtp());
        userDto.setOtpGeneratedTime(LocalDateTime.now());
        otpService.sendOTPEmail(userDto.getEmail(),userDto.getOtp());

    }

    @Override
    public void changeUsername(User user, String newUsername) {
        try {
                user.setUsername(newUsername);
                userRepository.save(user);
                updateSecurityContext(user,newUsername);
        }catch (Exception e){
            throw new RuntimeException("error");
        }


    }

    @Override
    public User getCurrentUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());

    }

    @Override
    public boolean checkCurrentPassword(User user, String oldPassword) {

        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


    @Override
    public void changePassword(User user, String newPassword) {

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateSecurityContext(User user, String newPassword) {
        UserDetails userDetails= userDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    public List<Address> getAddresses(User user) {
        System.out.println("get address method called");
       return addressRepository.findByUser(user);
    }

    @Override
    public int getTotalUsers() {
        return userRepository.findAll().stream().filter(user -> user.getRoles().equals("USER")).toList().size();
    }

    @Override
    public Boolean verifyReferralId(String referralId) {
        if(userRepository.findByReferralId(referralId)!=null){
            System.out.println(userRepository.findByReferralId(referralId));
            return true;
        }else{
            System.out.println(userRepository.findByReferralId(referralId));
            return false;
        }
    }

    @Override
    public Double getWalletBalance() {
        Wallet wallet=walletRepository.findByUser(getCurrentUser());
        return wallet.getBalance();
    }

    @Override
    public void payfromWallet(Double totalAmount) {
        try {
            Wallet wallet=walletRepository.findByUser(getCurrentUser());
            wallet.setBalance(wallet.getBalance()-totalAmount);
            walletRepository.save(wallet);
            Transactions transaction=new Transactions();
            transaction.setAmount(totalAmount);
            transaction.setWallet(wallet);
            transaction.setTransactionStatus(TransactionStatus.DEBIT);
            transaction.setTransactionTime(LocalDateTime.now());
            transaction.setTransactionType(TransactionType.ORDER);
            transactionRepository.save(transaction);
        }catch (Exception e){
            throw new NullPointerException("error from payFromWalletMethod");
        }
    }

    @Override
    public void cancelOrderMoneyReturn(Double amountPayed) {
        try {
            Wallet wallet=walletRepository.findByUser(getCurrentUser());
            wallet.setBalance(wallet.getBalance()+amountPayed);
            walletRepository.save(wallet);
            Transactions transaction=new Transactions();
            transaction.setAmount(amountPayed);
            transaction.setWallet(wallet);
            transaction.setTransactionStatus(TransactionStatus.CREDIT);
            transaction.setTransactionTime(LocalDateTime.now());
            transaction.setTransactionType(TransactionType.ORDER_CANCEL);
            transactionRepository.save(transaction);
        }catch (Exception e){
            throw new RuntimeException("error from cancelOrderMoneyReturn method");
        }
    }

    @Override
    public void addMoneytoWallet(Double amount) {
        try {

           Wallet wallet=walletRepository.findByUser(getCurrentUser());
            wallet.setBalance(wallet.getBalance()+amount);
            walletRepository.save(wallet);
            Transactions transaction=new Transactions();
            transaction.setAmount(amount);
            transaction.setWallet(wallet);
            transaction.setTransactionStatus(TransactionStatus.CREDIT);
            transaction.setTransactionTime(LocalDateTime.now());
            transaction.setTransactionType(TransactionType.ADD_MONEY);
            transactionRepository.save(transaction);
        }catch (Exception e){
            throw new RuntimeException("error from addMoneyToWalletMethod");
        }
    }

    @Override
    public List<TransactionDto> getAllWalletTransactions() {
       List<TransactionDto> transactionDtos=new ArrayList<>();
       List<Transactions>transactions=transactionRepository.findByWallet_WalletId(getCurrentUser().getWallet().getWalletId());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
       for(Transactions transaction:transactions){
           TransactionDto transactionDto=new TransactionDto();
           transactionDto.setAmount(String.valueOf(transaction.getAmount()));
           transactionDto.setTransactionStatus(String.valueOf(transaction.getTransactionStatus()));
           transactionDto.setDate(transaction.getTransactionTime().format(dateFormatter));
           transactionDto.setTime(transaction.getTransactionTime().format(timeFormatter));
           transactionDto.setTransactionType(String.valueOf(transaction.getTransactionType()));
           transactionDtos.add(transactionDto);
       }
       return transactionDtos;
    }


}

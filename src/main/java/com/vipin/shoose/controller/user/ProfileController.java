package com.vipin.shoose.controller.user;

import com.vipin.shoose.dto.AddressDto;
import com.vipin.shoose.dto.TransactionDto;
import com.vipin.shoose.model.Address;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.service.AddressService;
import com.vipin.shoose.service.UserService;
import com.vipin.shoose.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class ProfileController {
    @Autowired
    AddressService addressService;
    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/profile")
    public String userProfile(Model model){
        model.addAttribute("newAddress",new AddressDto());
        model.addAttribute("addresses",addressService.getCurrentUserAddresses());

        model.addAttribute("user", userService.getCurrentUser());
        return "user/profile";

    }
    @PostMapping("/change-username")
    public String changeUsername(@RequestParam("newUsername") String newUsername,
                                 RedirectAttributes redirectAttributes){

        User user=userService.getCurrentUser();
        if(Objects.equals(newUsername, "")){
            redirectAttributes.addFlashAttribute("null","enter a new username");
            return "redirect:/user/profile";
        }if(Objects.equals(user.getUsername(), newUsername)){
            redirectAttributes.addFlashAttribute("equal","this is your current username. enter a new username.");
            return "redirect:/user/profile";
        }
        if(userRepository.existsByUsername(newUsername)){
            redirectAttributes.addFlashAttribute("exists","user exists with this username. enter another username.");
            return "redirect:/user/profile";
        }
        userService.changeUsername(user,newUsername);
        redirectAttributes.addFlashAttribute("success","You have Successfully changed your username");
        redirectAttributes.addAttribute("user",userService.getCurrentUser());
        return "redirect:/user/profile";
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes){
        User user=userService.getCurrentUser();
     if(userService.checkCurrentPassword(user,oldPassword)){
         userService.changePassword(user,newPassword);
         redirectAttributes.addFlashAttribute("changePasswordSuccess","Your Password Changed successfully");
         return "redirect:/user/profile";
        }
        redirectAttributes.addFlashAttribute("passwordMismatch","the password you have entered is not your current password");
        return "redirect:/user/profile";

    }

    @PostMapping("/add-address")
    public  String addAddress(AddressDto addressDto){
        addressService.save(addressDto);
        return "redirect:/user/profile";
    }
    @PostMapping("/address/delete/{addressId}")
    public String deleteAddress(@PathVariable("addressId") Long addressId,
                                RedirectAttributes redirectAttributes){
        addressService.deleteAddress(addressId);
        redirectAttributes.addFlashAttribute("adDeleteSuccess","Address Deleted");
        return "redirect:/user/profile";
    }
    @PostMapping("/address/edit/{addressId}")
    public String editAddress(AddressDto addressDto,
                              @PathVariable("addressId") Long addressId,
                                RedirectAttributes redirectAttributes){
        addressService.editAddress(addressId,addressDto);
        redirectAttributes.addFlashAttribute("editAddress","Address Updated");
        return "redirect:/user/profile";
    }
    @GetMapping ("/wishlist")
    public String userWishlist(){
        return "user/wishlist";
    }
    @GetMapping ("/orders")
    public String userOrders(){
        return "user/orders";
    }
    @GetMapping ("/wallet")
    public String userWallet(Model model){
        model.addAttribute("transactions",userService.getAllWalletTransactions());
       Double balance=userService.getWalletBalance();
        model.addAttribute("balance",balance);
        return "user/wallet";
    }
    @PostMapping("/addMoney")
    public String addMoney(@RequestParam("amount")Double amount){
        userService.addMoneytoWallet(amount);
        return "redirect:/user/wallet";
    }
    @GetMapping("/getWalletBalance")
    public ResponseEntity<Double>getWalletBalance(){
        return new ResponseEntity<>( userService.getWalletBalance(), HttpStatus.OK);
    }



}

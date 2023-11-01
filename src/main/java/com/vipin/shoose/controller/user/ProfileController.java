package com.vipin.shoose.controller.user;

import com.vipin.shoose.dto.AddressDto;
import com.vipin.shoose.model.Address;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.service.AddressService;
import com.vipin.shoose.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
public class ProfileController {
    @Autowired
    AddressService addressService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @GetMapping ("/user/profile")
    public String userProfile(Model model){
        model.addAttribute("newAddress",new AddressDto());
        model.addAttribute("addresses",addressService.getCurrentUserAddresses());
        model.addAttribute("user",userService.getCurrentUser());
        return "/user/profile";

    }
    @PostMapping("/user/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes){
        User user=userService.getCurrentUser();
        System.out.println(user.getPassword());
        System.out.println(userService.checkCurrentPassword(user,oldPassword));
     if(userService.checkCurrentPassword(user,oldPassword)){
         userService.changePassword(user,newPassword);
         redirectAttributes.addFlashAttribute("changePasswordSuccess","Your Password Changed successfully");
         return "redirect:/user/profile";
        }
        redirectAttributes.addFlashAttribute("passwordMismatch","the password you have entered is not your current password");
        return "redirect:/user/profile";

    }

    @PostMapping("/user/add-address")
    public  String addAddress(AddressDto addressDto){
        addressService.save(addressDto);
        return "redirect:/user/profile";
    }
    @PostMapping("user/address/delete/{addressId}")
    public String deleteAddress(@PathVariable("addressId") Long addressId,
                                RedirectAttributes redirectAttributes){
        addressService.deleteAddress(addressId);
        redirectAttributes.addFlashAttribute("adDeleteSuccess","Address Deleted");
        return "redirect:/user/profile";
    }
    @PostMapping("user/address/edit/{addressId}")
    public String editAddress(AddressDto addressDto,
                              @PathVariable("addressId") Long addressId,
                                RedirectAttributes redirectAttributes){
        addressService.editAddress(addressId,addressDto);
        redirectAttributes.addFlashAttribute("editAddress","Address Updated");
        return "redirect:/user/profile";
    }
    @PostMapping("/user/change-username")
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
        redirectAttributes.addFlashAttribute("success","this is your current username. enter a new username.");
        redirectAttributes.addAttribute(user);
        return "redirect:/user/profile";
    }

    @GetMapping ("/user/cart")
    public String userCart(Model model){
        User user=userService.getCurrentUser();
        model.addAttribute("user",user);
        return "/user/cart";
    }
    @GetMapping ("/user/wishlist")
    public String userWishlist(){
        return "/user/wishlist";
    }
    @GetMapping ("/user/orders")
    public String userOrders(){
        return "/user/orders";
    }
    @GetMapping ("/user/wallet")
    public String userWallet(){
        return "/user/wallet";
    }



}

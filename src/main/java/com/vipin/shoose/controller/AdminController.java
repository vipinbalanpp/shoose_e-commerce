package com.vipin.shoose.controller;

import com.vipin.shoose.model.User;
import com.vipin.shoose.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    UserService userService;
    @GetMapping("admin/home")
    public String home(){
        return "admin/home";
    }


    @GetMapping("admin/add-poduct")
    public String addProduct(){
        return "admin/add-product";
    }
    @GetMapping("admin/customers-list")
    public String customerList(Model model){
        List<User> allUsers=userService.getAllUsers();
        List<User>users=new ArrayList<>();
        for(User user:allUsers){
            if ("USER".equals(user.getRoles())) {
                users.add(user);
            }
        }
        model.addAttribute("users",users);
        return "admin/customers-list";
    }
    @GetMapping("admin/edit-product")
    public String editProduct(){
        return "admin/edit-product";
    }
    @GetMapping("admin/products-list")
    public String productList(){
        return "admin/product-list";
    }
    @GetMapping("admin/orders-list")
    public String ordersList(){
        return "admin/orders-list";
    }
    @GetMapping("admin/coupons")
    public String coupons(){
        return "admin/coupon";
    }
    @GetMapping("admin/banners")
    public String banners(){
        return "admin/banners";
    }
}

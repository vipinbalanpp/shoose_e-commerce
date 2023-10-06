package com.vipin.shoose.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("admin/home")
    public String home(){
        return "admin/home";
    }
    @GetMapping("admin/add-poduct")
    public String addProduct(){
        return "admin/add-product";
    }
    @GetMapping("admin/customers-list")
    public String customerList(){
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

package com.vipin.shoose.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("admin/orders-list")
    public String ordersList(){
        return "admin/orders-list";
    }
}

package com.vipin.shoose.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserOrderController {
    @GetMapping("/order-success")
    public String orderSuccess(){
        return "user/order-success";
    }
}

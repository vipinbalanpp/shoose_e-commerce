package com.vipin.shoose.controller.admin;

import com.vipin.shoose.model.User;
import com.vipin.shoose.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class CustomerController {
    @Autowired
    UserService userService;

    @GetMapping("/list")
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
    } @PostMapping("/block-user/{id}")
    public String blockUser(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        userService.blockUser(id);
        String blocked="user blocked";
        redirectAttributes.addFlashAttribute("blocked",blocked);
        return "redirect:/admin/customers-list";
    }

    @PostMapping("/unblock-user/{id}")
    public String unblockUser(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        userService.unBlockUser(id);
        String unBlocked="user unblocked";
        redirectAttributes.addFlashAttribute("unblocked",unBlocked);
        return "redirect:/admin/customers-list";
    }
}

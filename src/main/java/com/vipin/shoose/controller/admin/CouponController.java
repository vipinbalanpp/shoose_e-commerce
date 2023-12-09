package com.vipin.shoose.controller.admin;

import com.vipin.shoose.dto.CouponDto;
import com.vipin.shoose.repository.CouponRepository;
import com.vipin.shoose.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CouponController {
    @Autowired
    CouponService couponService;
    @Autowired
    CouponRepository couponRepository;
    @GetMapping("admin/coupons")
    public String coupons(Model model){
       model.addAttribute("coupons",couponService.getAllCoupons());
        return "admin/coupon";
    }
    @PostMapping("/admin/add-coupon")
    public String addNewCoupon(CouponDto couponDto,
                               RedirectAttributes redirectAttributes){
        couponService.addNewCoupon(couponDto);
        redirectAttributes.addFlashAttribute("addingSuccess","coupon added successfully");
        return "redirect:/admin/coupons";
    }
}

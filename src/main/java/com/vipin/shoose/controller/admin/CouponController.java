package com.vipin.shoose.controller.admin;

import com.vipin.shoose.dto.CouponDto;
import com.vipin.shoose.repository.CouponRepository;
import com.vipin.shoose.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.lang.model.element.NestingKind;

@Controller
@RequestMapping("/admin")
public class CouponController {
    @Autowired
    CouponService couponService;
    @Autowired
    CouponRepository couponRepository;
    @GetMapping("/coupons")
    public String coupons(Model model){
       model.addAttribute("coupons",couponService.getAllCoupons());
        return "admin/coupon";
    }
    @PostMapping("/add-coupon")
    public String addNewCoupon(CouponDto couponDto,
                               RedirectAttributes redirectAttributes){
        if(couponService.getCouponByCouponCode(couponDto.getCouponCode())){
            redirectAttributes.addFlashAttribute("couponExists","Coupon code Exists");
            return "redirect:/admin/coupons";
        }
        couponService.addNewCoupon(couponDto);
        redirectAttributes.addFlashAttribute("addingSuccess","coupon added successfully");
        return "redirect:/admin/coupons";
    }
    @PostMapping("delete-coupon")
    public String deleteCoupon(@RequestParam("couponId") Long couponId){
        couponService.deleteCoupon(couponId);
        return "redirect:/admin/coupons";
    }
}

package com.vipin.shoose.service;

import com.vipin.shoose.dto.CouponDto;
import com.vipin.shoose.model.Coupon;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService{
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    UserService userService;
    @Override
    public void addNewCoupon(CouponDto couponDto) {
        Coupon coupon=new Coupon();
        coupon.setCouponName(couponDto.getCouponName());
        coupon.setCouponCode(couponDto.getCouponCode());
        coupon.setStartDate(couponDto.getStartDate());
        coupon.setExpiryDate(couponDto.getExpiryDate());
        coupon.setDiscountPercentage(couponDto.getDiscountPercentage());
        coupon.setMinimumPurchaseAmount(couponDto.getMinimumPurchaseAmount());
        System.out.println(coupon);
        couponRepository.save(coupon);

    }

    @Override
    public List<Coupon> getAllCoupons() {
       return couponRepository.findAll();
    }

    @Override
    public Boolean checkCouponExists(String couponCode) {
        try {
            if(couponRepository.existsByCouponCode(couponCode)) {
                LocalDate currentDate = LocalDate.now();
                Coupon coupon = couponRepository.findByCouponCode(couponCode);
                if(!currentDate.isAfter(coupon.getExpiryDate())){
                    return !coupon.getUsedCustomers().contains(userService.getCurrentUser());
                }
            }return false;
        }catch (Exception e){
            throw  new RuntimeException();
        }

    }

    @Override
    public Float applyCoupon(String couponCode, float totalAmount) {
        try {
            Coupon coupon=couponRepository.findByCouponCode(couponCode);
            return totalAmount-coupon.getDiscountPercentage();
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public float getDisCountAmount(String couponCode) {
        return couponRepository.findByCouponCode(couponCode).getDiscountPercentage();
    }

    @Override
    public void couponApplied(String couponCode, User currentUser) {
        try {
            Coupon coupon=couponRepository.findByCouponCode(couponCode);
            List<User>usedCustomers=new LinkedList<>();
            usedCustomers.add(currentUser);
            coupon.setUsedCustomers(usedCustomers);
            couponRepository.save(coupon);
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public Float getMinumumPurchaseAmount(String couponCode) {
        return couponRepository.findByCouponCode(couponCode).getMinimumPurchaseAmount();
    }

    @Override
    public void deleteCoupon(Long couponId) {
        try {
            couponRepository.delete(couponRepository.findByCouponId(couponId));
        }catch (Exception e){
            throw  new RuntimeException();
        }
    }

    @Override
    public boolean getCouponByCouponCode(String couponCode) {
        Coupon coupon=couponRepository.findByCouponCode(couponCode);
        return coupon != null;
    }

    @Override
    public void editCoupon(CouponDto couponDto) {
        try {
            Coupon coupon=couponRepository.findByCouponId(couponDto.getCouponId());
            coupon.setCouponName(couponDto.getCouponName());
            coupon.setCouponCode(couponDto.getCouponCode());
            coupon.setStartDate(couponDto.getStartDate());
            coupon.setExpiryDate(couponDto.getExpiryDate());
            coupon.setDiscountPercentage(couponDto.getDiscountPercentage());
            coupon.setMinimumPurchaseAmount(couponDto.getMinimumPurchaseAmount());
            System.out.println(coupon);
            couponRepository.save(coupon);
        }catch (Exception e){
            throw  new RuntimeException();
        }
    }
}

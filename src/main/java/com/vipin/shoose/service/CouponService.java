package com.vipin.shoose.service;

import com.vipin.shoose.dto.CouponDto;
import com.vipin.shoose.model.Coupon;
import com.vipin.shoose.model.User;

import java.util.List;

public interface CouponService {
    void addNewCoupon(CouponDto couponDto);

    List<Coupon> getAllCoupons();

    Boolean checkCouponExists(String couponCode);

    Float applyCoupon(String couponCode, float totalAmount);

    float getDisCountAmount(String couponCode);

    void couponApplied(String couponCode, User currentUser);

    Float getMinumumPurchaseAmount(String couponCode);

    void deleteCoupon(Long couponId);

    boolean getCouponByCouponCode(String couponCode);
}

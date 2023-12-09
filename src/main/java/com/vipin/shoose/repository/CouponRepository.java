package com.vipin.shoose.repository;

import com.vipin.shoose.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {
    boolean existsByCouponCode(String couponCode);

    Coupon findByCouponCode(String couponCode);
}

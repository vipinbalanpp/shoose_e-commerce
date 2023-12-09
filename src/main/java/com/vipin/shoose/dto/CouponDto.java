package com.vipin.shoose.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
    private Long couponId;
    private String couponName;
    private String couponCode;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private int discountPercentage;
    private Float minimumPurchaseAmount;
}

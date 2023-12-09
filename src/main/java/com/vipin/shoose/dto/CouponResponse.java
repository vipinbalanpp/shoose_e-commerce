package com.vipin.shoose.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponResponse {
    private float discountAmount;
    private float totalAmount;
    private boolean isAbleToApply;
}

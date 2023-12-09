package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;
    private String couponName;
    private String couponCode;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private int discountPercentage;
    private Float minimumPurchaseAmount;
    @ManyToMany
    @JoinTable(
            name = "coupon_user",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User>usedCustomers;

}

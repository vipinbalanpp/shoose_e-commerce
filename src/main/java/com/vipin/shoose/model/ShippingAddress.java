package com.vipin.shoose.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.Data;

@Entity
@Data
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingAddressId;
    private String fullName;
    private String phoneNumber;
    private String buildingName;
    private String streetName;
    private String city;
    private String state;
    private String postalCode;
}

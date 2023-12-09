package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private  String fullName;
    private String phoneNumber;
    private  String buildingName;
    private String streetName;
    private String city;
    private  String state;
    private  String postalCode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;
}

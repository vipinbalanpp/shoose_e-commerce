package com.vipin.shoose.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private  String fullName;
    private String phoneNumber;
    private  String buildingName;
    private String streetName;
    private String city;
    private  String state;
    private  String postalCode;
}

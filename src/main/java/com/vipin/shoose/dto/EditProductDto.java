package com.vipin.shoose.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditProductDto {
    private  String productName;
    private String description;
    private String gender;
    private  String  brand;
    private String price;
    private  Long  categoryId;
}

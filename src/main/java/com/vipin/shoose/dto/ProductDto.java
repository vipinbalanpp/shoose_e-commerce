package com.vipin.shoose.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private  Long id;
    private  String productName;
    private String description;
    private String gender;
    private  String  brand;
    private MultipartFile image;
    private Integer price;
    private  Long  categoryId;
    private String categoryName;
    private List<VariantDto> variants;


}

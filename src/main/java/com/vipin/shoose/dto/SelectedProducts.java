package com.vipin.shoose.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedProducts {
    private Long variantId;
    private Long productId;
    private String productName;
    private String brand;
    private String color;
    private Integer actualPrice;
    private Integer discountPrice;
    private Boolean isCategoryHavingOffer=false;
    private String image;
    private Long size;
    private  Long quantity=0L;
    private Long stock;
    private Integer amountToBePayed=0;
    private Integer actualTotalAmount;
    private Integer totalDiscountAmount;


}

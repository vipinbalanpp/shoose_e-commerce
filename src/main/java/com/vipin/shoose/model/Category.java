package com.vipin.shoose.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long categoryId;
    private String categoryName;
    private  String description;
    private  String image;
    private Boolean isHavingOffer=false;
    private Integer offerInPercentage;
    private LocalDate startDate;
    private LocalDate expiryDate;
    boolean isEnabled=false;

}

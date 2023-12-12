package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long productId;
    private String productName;
    private String description;
    private  String gender;
    private  String brand;
    private Integer actualPrice;
    private Integer discountPrice;
    private String image;
    private Boolean isCategoryHavingOffer=false;
    private Long quantity;
    private LocalDateTime lastUpdated;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Variant>variants;
    boolean isEnabled=false;
}

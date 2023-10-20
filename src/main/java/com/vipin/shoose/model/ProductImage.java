package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long imageId;
    String color;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}

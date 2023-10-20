package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String color;
    private String size;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String quantity;
}

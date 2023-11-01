package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variantId;
    private String color;
    private Long size;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Long quantity;
}

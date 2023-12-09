package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Setter
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ElementCollection
    @CollectionTable(name = "cart_product_mapping",
            joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "variant_id")
    @Column(name = "quantity")
    private Map<Variant, Long> products;

    public Cart() {
        this.products = new LinkedHashMap<>();
    }

    public void setProducts(Variant variant, Long i) {
        products.put(variant,i);
    }
}

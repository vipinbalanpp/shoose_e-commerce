package com.vipin.shoose.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long colorId;
    private  String colorName;
}

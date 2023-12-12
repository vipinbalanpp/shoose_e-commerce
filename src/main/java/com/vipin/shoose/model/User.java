package com.vipin.shoose.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotNull
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    @NotNull
    private String email;
    private  String password;
    private  String roles;
    private String image;
    private  boolean isEnabled=false;
    private  LocalDateTime createdTime;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address>addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OrderInfo> orders;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    private String referralId;
}

package com.vipin.shoose.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
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
    private  boolean isEnabled=false;
    private  LocalDateTime createdTime;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address>addresses;
}

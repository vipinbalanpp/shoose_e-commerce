package com.vipin.shoose.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int id;
    private String username;
    private String email;
    private String password;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    private String referalId;

}
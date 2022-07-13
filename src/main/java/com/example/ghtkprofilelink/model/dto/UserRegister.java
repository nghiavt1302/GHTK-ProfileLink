package com.example.ghtkprofilelink.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegister {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String mail;
}

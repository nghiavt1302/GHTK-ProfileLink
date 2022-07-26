package com.example.ghtkprofilelink.model.dto;

import com.example.ghtkprofilelink.constants.RoleEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String mail;
    private RoleEnum role;
}

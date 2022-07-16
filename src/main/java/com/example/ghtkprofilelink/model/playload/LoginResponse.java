package com.example.ghtkprofilelink.model.playload;

import com.example.ghtkprofilelink.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class LoginResponse {
    private String jwt;
    private UserEntity user;
}

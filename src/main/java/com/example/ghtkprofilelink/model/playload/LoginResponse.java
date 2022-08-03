package com.example.ghtkprofilelink.model.playload;

import com.example.ghtkprofilelink.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class LoginResponse {
    private String jwt;
    private UserDto user;
}

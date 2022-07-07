package com.example.ghtkprofilelink.model.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Integer role;
}

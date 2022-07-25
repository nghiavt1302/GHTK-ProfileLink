package com.example.ghtkprofilelink.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    USER(0), USER_VIP(1), ADMIN(2);
    private final int value;
}

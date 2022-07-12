package com.example.ghtkprofilelink.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    INACTIVE(0), ACTIVE(1);
    private final int value;
}

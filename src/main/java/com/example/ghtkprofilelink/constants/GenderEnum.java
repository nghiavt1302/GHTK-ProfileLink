package com.example.ghtkprofilelink.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenderEnum {
    MALE(0),
    FEMALE(1),
    OTHER(2);

    private final int gender;
}

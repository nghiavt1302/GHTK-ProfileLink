package com.example.ghtkprofilelink.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ButtonTypeEnum {
    CIRCLE_SOLID(0),
    CIRCLE_REGULAR(1),
    RECTANGLE_SOLID(2),
    RECTANGLE_REGULAR(3);

    private final int type;
}

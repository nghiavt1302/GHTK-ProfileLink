package com.example.ghtkprofilelink.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BackgroundTypeEnum {
    COLOR(0),
    IMAGE(1);

    private final int type;
}

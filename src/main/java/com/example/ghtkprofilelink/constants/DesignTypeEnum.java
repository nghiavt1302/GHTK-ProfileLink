package com.example.ghtkprofilelink.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DesignTypeEnum {
    DEFAULT(0),
    USER_CREATE(1);

    private final int type;
}

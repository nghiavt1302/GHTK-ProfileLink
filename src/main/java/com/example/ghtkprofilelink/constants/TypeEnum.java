package com.example.ghtkprofilelink.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeEnum {
    LINK(0),
    HEADER(1),
    PLUGIN(2);

    private final int type;
}

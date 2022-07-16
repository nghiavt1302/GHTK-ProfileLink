package com.example.ghtkprofilelink.model.dto;

import com.example.ghtkprofilelink.constants.BackgroundTypeEnum;
import com.example.ghtkprofilelink.constants.ButtonTypeEnum;
import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DesignDto {
    private Long id;
    private String name;
    private DesignTypeEnum type;
    @JsonProperty("background_type")
    private BackgroundTypeEnum backgroundType;
    @JsonProperty("background_color")
    private String backgroundColor;
    @JsonProperty("background_image")
    private String backgroundImage;
    private String font;
    @JsonProperty("button_type")
    private ButtonTypeEnum buttonType;
    @JsonProperty("button_color")
    private String buttonColor;
    private String picture;
    @JsonProperty("text_color")
    private String textColor;
}

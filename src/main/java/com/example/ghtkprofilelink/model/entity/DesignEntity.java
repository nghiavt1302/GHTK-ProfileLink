package com.example.ghtkprofilelink.model.entity;

import com.example.ghtkprofilelink.constants.BackgroundTypeEnum;
import com.example.ghtkprofilelink.constants.ButtonTypeEnum;
import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "designs")
public class DesignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private StatusEnum status;

    public DesignEntity setValueByDto(DesignDto designDto) {
        this.id = designDto.getId();
        this.name = designDto.getName();
        this.type = designDto.getType();
        this.backgroundType = designDto.getBackgroundType();
        this.backgroundColor = designDto.getBackgroundColor();
        this.backgroundImage = designDto.getBackgroundImage();
        this.font = designDto.getFont();
        this.buttonType = designDto.getButtonType();
        this.buttonColor = designDto.getButtonColor();
        this.picture=designDto.getPicture();
        this.textColor = designDto.getTextColor();

        return this;
    }
}

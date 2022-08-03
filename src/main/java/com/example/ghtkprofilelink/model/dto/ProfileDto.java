package com.example.ghtkprofilelink.model.dto;

import com.example.ghtkprofilelink.constants.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private Long id;
    private String fullname;
    @JsonProperty("short_bio")
    private String shortBio;
    @JsonProperty("about")
    private String about;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private GenderEnum gender;
    private String location;
    @JsonProperty("avatar_link")
    private String avatarLink;
    @JsonProperty("click_count")
    private Integer clickCount;
    @JsonProperty("design_id")
    private Long designId;
}

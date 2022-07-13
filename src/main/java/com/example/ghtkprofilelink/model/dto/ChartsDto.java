package com.example.ghtkprofilelink.model.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartsDto {
    private Long id;
    @JsonProperty("click_count")
    private Long clickCount;
    @JsonProperty("profile_id")
    private Integer profileId;
    private Date date;
    private String country;
}

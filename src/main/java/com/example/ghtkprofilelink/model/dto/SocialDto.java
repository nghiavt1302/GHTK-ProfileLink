package com.example.ghtkprofilelink.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SocialDto {
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("icon")
    private String icon;
    private String link;
    @JsonProperty("profile_id")
    private Long profileId;
    @JsonProperty("click_count")
    private Long clickCount;
}

package com.example.ghtkprofilelink.model.dto;

import com.example.ghtkprofilelink.constants.GenderEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Long id;
    private String name;
    @JsonProperty("short_bio")
    private String shortBio;
    @JsonProperty("about_me")
    private String aboutMe;
    private LocalDate birthday;
    private GenderEnum gender;
    private String location;
    @JsonProperty("link_profile")
    private String linkProfile;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("click_count")
    private Integer clickCount;
}

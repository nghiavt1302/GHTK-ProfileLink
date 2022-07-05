package com.example.ghtkprofilelink.model.entity;

import com.example.ghtkprofilelink.constants.GenderEnum;
import com.example.ghtkprofilelink.model.dto.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public ProfileEntity setValueFromDTO(ProfileDTO profileDTO){
        this.id=profileDTO.getId();
        this.name=profileDTO.getName();
        this.shortBio=profileDTO.getShortBio();
        this.aboutMe=profileDTO.getAboutMe();
        this.birthday=profileDTO.getBirthday();
        this.gender=profileDTO.getGender();
        this.location=profileDTO.getLocation();

        return this;
    }
}

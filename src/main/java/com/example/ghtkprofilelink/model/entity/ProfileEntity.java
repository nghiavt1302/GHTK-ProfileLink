package com.example.ghtkprofilelink.model.entity;

import com.example.ghtkprofilelink.constants.GenderEnum;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String fullname;
    @JsonProperty("short_bio")
    private String shortBio;
    @JsonProperty("about")
    private String about;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private GenderEnum gender;
    private String location;
    @JsonProperty("profile_link")
    private String profileLink;
    @JsonProperty("avatar_link")
    private String avatarLink;
    @JsonProperty("click_count")
    private Integer clickCount;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("design_id")
    private Long designId;
    private StatusEnum status;

    public ProfileEntity setValueFromDto(ProfileDto profileDto) {
        this.id = profileDto.getId();
        this.fullname = profileDto.getFullname();
        this.shortBio = profileDto.getShortBio();
        this.about = profileDto.getAbout();
        this.birthday = profileDto.getBirthday();
        this.gender = profileDto.getGender();
        this.location = profileDto.getLocation();
        this.profileLink = profileDto.getProfileLink();
        this.userId = profileDto.getUserId();
        this.designId = profileDto.getDesignId();

        return this;
    }
}

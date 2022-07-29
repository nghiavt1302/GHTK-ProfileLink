package com.example.ghtkprofilelink.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.SocialDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "socials")
public class SocialEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("social_name")
    @Column(name = "social_name")
    private String name;
    @JsonProperty("social_icon")
    @Column(name = "social_icon")
    private String icon;
    private String links;
    @JsonProperty("profile_id")
    @Column(name = "profile_id")
    private Long profileId;
    @JsonProperty("click_count")
    @Column(name = "click_count")
    private Long clickCount;

    public SocialEntity setValueFromDto(SocialDto socialDto) {
        this.id = socialDto.getId();
        this.name = socialDto.getName();
        this.icon = socialDto.getIcon();
        this.links = socialDto.getLinks();
        this.profileId = socialDto.getProfileId();
        this.clickCount = socialDto.getClickCount();

        return this;
    }
}

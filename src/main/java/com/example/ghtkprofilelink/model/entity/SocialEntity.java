package com.example.ghtkprofilelink.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.ghtkprofilelink.model.dto.SocialDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "social")
public class SocialEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public SocialEntity setValueFromDto(SocialDto socialDto) {
        this.id = socialDto.getId();
        this.name = socialDto.getName();
        this.icon = socialDto.getIcon();
        this.link = socialDto.getLink();
        this.profileId = socialDto.getProfileId();
        this.clickCount = socialDto.getClickCount();

        return this;
    }
}

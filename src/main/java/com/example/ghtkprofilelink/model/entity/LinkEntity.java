package com.example.ghtkprofilelink.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.constants.TypeEnum;
import com.example.ghtkprofilelink.model.dto.LinkDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "links")
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TypeEnum type;
    private String title;
    private String url;
    private String picture;
    @JsonProperty("profile_id")
    private Long profileId;
    @JsonProperty("click_count")
    private Long clickCount;

    public LinkEntity setValueFromDto(LinkDto linkDto) {
        this.id = linkDto.getId();
        this.type = linkDto.getType();
        this.title = linkDto.getTitle();
        this.url = linkDto.getUrl();
        this.picture = linkDto.getPicture();
        this.profileId = linkDto.getProfileId();
        this.clickCount = linkDto.getClickCount();

        return this;
    }
}

package com.example.ghtkprofilelink.model.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.ghtkprofilelink.model.dto.ChartsDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "charts")
@AllArgsConstructor
@NoArgsConstructor
public class ChartsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("click_count")
    @Column(name = "click_count")
    private Long clickCount;
    @JsonProperty("profile_id")
    @Column(name = "profile_id")
    private Integer profileId;
    private Date date;
    private String country;

    public ChartsEntity setValueByDto(ChartsDto chartsDto) {
        this.id = chartsDto.getId();
        this.clickCount = chartsDto.getClickCount();
        this.profileId = chartsDto.getProfileId();
        this.date = chartsDto.getDate();
        this.country = chartsDto.getCountry();

        return this;
    }
}

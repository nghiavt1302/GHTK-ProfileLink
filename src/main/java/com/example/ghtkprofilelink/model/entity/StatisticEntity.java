package com.example.ghtkprofilelink.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.StatisticDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "statistics")
@AllArgsConstructor
@NoArgsConstructor
public class StatisticEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("click_count")
    @Column(name = "click_count")
    private Long clickCount;
    @JsonProperty("profile_id")
    @Column(name = "profile_id")
    private Integer profileId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public StatisticEntity setValueByDto(StatisticDto statisticDto) {
        this.id = statisticDto.getId();
        this.clickCount = statisticDto.getClickCount();
        this.profileId = statisticDto.getProfileId();
        this.date = statisticDto.getDate();

        return this;
    }
}

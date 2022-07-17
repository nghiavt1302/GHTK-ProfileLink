package com.example.ghtkprofilelink.model.dto;



import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String country;
}

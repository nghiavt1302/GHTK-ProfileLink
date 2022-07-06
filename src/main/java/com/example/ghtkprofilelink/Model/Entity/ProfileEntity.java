package com.example.ghtkprofilelink.Model.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String avatar;
    private String profileLink;
    private Integer userId;
    private String fullname;
    private String shortBio;
    private String about;
    private Date birthday;
    private String gender;
    private String location;
    private Long clickCount;
}

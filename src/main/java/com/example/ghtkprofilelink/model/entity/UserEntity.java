package com.example.ghtkprofilelink.model.entity;

import com.example.ghtkprofilelink.constants.StatusEnum;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "role")
    private Integer role;
}

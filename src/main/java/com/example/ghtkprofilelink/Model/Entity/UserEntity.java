package com.example.ghtkprofilelink.Model.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private Integer status;
    @Column(name = "role_id")
    private Integer roleId;
}

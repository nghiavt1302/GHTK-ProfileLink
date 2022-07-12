package com.example.ghtkprofilelink.model.entity;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
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

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "role")
    private Integer role;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    private String mail;

    public UserEntity mapUserDto(UserDto userDto) {
        this.setId(null);
        this.setUsername(userDto.getUsername());
        this.setStatus(StatusEnum.ACTIVE);
//        this.setRole(userDto.getRole());
        return this;
    }

    public UserEntity mapUserRegister(UserRegister userRegister) {
        this.setId(null);
        this.setUsername(userRegister.getUsername());
        this.setStatus(StatusEnum.ACTIVE);
        this.setMail(userRegister.getMail());
//        this.setRole(userDto.getRole());
        return this;
    }
}

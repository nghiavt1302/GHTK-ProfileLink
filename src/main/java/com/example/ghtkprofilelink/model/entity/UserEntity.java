package com.example.ghtkprofilelink.model.entity;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
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

    public UserEntity mapUserDto(UserDto userDto) {
        this.setId(null);
        this.setUsername(userDto.getUsername());
        this.setStatus(StatusEnum.ACTIVE);
        this.setRole(userDto.getRole());
        return this;
    }
}

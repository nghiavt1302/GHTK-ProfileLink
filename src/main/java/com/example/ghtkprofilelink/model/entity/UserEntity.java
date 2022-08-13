package com.example.ghtkprofilelink.model.entity;

import com.example.ghtkprofilelink.constants.ProviderEnum;
import com.example.ghtkprofilelink.constants.RoleEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
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

//    @Column(name = "status")
//    private StatusEnum status;

    @Column(name = "role")
    private RoleEnum role;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    @JsonProperty("is_profile")
    private Boolean isProfile;

    @Column(unique = true)
    private String mail;

    @Column(name = "update_password_token", length = 64)
    private String updatePasswordToken;

    @Column(name = "is_upgrade_role", columnDefinition = "boolean default false")
    private Boolean isUpgradeRole = false;

    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    public ProviderEnum getProvider(){
        return provider;
    }

    public void setProvider(ProviderEnum provider) {
        this.provider = provider;
    }


    public UserEntity mapUserDto(UserDto userDto) {
        this.setId(null);
        this.setUsername(userDto.getUsername());
//        this.setStatus(StatusEnum.ACTIVE);
//        this.setRole(userDto.getRole());
        return this;
    }

    public UserEntity mapUserRegister(UserRegister userRegister) {
        this.setId(null);
        this.setUsername(userRegister.getUsername());
//        this.setStatus(StatusEnum.ACTIVE);
        this.setMail(userRegister.getMail());
//        this.setRole(userDto.getRole());
        return this;
    }
}

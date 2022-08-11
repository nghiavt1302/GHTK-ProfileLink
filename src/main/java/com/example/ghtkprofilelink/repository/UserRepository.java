package com.example.ghtkprofilelink.repository;

import com.example.ghtkprofilelink.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

//    Page<UserEntity> findByStatusEquals(StatusEnum statusEnum, Pageable pageable);

    Optional<UserEntity> findByVerificationCode(String verificationCode);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity getUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.mail = :email")
    UserEntity getUserByEmail(@Param("email") String email);

    Optional<UserEntity> findByMail(String mail);

    Optional<UserEntity> findByUpdatePasswordToken(String token);

    Page<UserEntity> findByIsUpgradeRoleAndUsernameContaining(Boolean isUpgradeRole,String username, Pageable pageable);

    Page<UserEntity> findByUsernameContaining(String username, Pageable pageable);
}

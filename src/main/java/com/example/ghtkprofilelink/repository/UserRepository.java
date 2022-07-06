package com.example.ghtkprofilelink.repository;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Page<UserEntity> findByStatusEquals(StatusEnum statusEnum, Pageable pageable);
}

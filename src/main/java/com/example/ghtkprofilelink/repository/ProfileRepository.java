package com.example.ghtkprofilelink.repository;

import com.example.ghtkprofilelink.model.entity.ProfileEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    ProfileEntity getProfileByShortBio(String shortBio);
}

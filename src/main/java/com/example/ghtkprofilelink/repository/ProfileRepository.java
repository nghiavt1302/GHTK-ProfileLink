package com.example.ghtkprofilelink.repository;

import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity,Long> {
}

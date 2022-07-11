package com.example.ghtkprofilelink.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.entity.SocialEntity;
import com.google.common.base.Optional;

@Repository
public interface SocialRepository extends JpaRepository<SocialEntity, Long> {
    Page<SocialEntity> findByProfileId(Pageable pageable, @Param("profile_id") Long profileId);
}

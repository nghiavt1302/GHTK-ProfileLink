package com.example.ghtkprofilelink.repository;

import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    ProfileEntity getProfileByShortBio(String shortBio);

    @Query("SELECT p FROM ProfileEntity p ORDER BY p.clickCount DESC")
    Page<ProfileEntity> getTopProfile(Pageable pageable);

}

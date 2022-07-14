package com.example.ghtkprofilelink.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.ghtkprofilelink.model.entity.ChartsEntity;

@Repository
public interface ChartsRepository extends JpaRepository<ChartsEntity, Long> {

    ChartsEntity findAllById(Long profileId);

    List<ChartsEntity> findAllByProfileId(Integer profileId);

    // Page<ChartsEntity> findByProfileId(Pageable pageable, @Param("profileId") Integer profileId);

}

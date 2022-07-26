package com.example.ghtkprofilelink.repository;

import java.util.List;

import com.example.ghtkprofilelink.constants.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.ghtkprofilelink.model.entity.ChartsEntity;

@Repository
public interface ChartsRepository extends JpaRepository<ChartsEntity, Long> {

    ChartsEntity findAllById(Long profileId);

    List<ChartsEntity> findAllByProfileId(Integer profileId);
    // List<ChartsEntity> findAllByStatus(Pageable pageable, @Param("status")
    // StatusEnum status);

    // Page<ChartsEntity> findByProfileId(Pageable pageable, @Param("profileId")
    // Integer profileId);
    @Query("SELECT p FROM ChartsEntity p where month(date) = month(now()) - 1 and year(date) = year(now()) ORDER BY p.clickCount DESC")
    Page<ChartsEntity> getTopProfileToMonth(Pageable pageable);
}

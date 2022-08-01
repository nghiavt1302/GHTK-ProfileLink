package com.example.ghtkprofilelink.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.ghtkprofilelink.model.entity.StatisticEntity;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    StatisticEntity findAllById(Long profileId);

    List<StatisticEntity> findAllByProfileId(Integer profileId);
    // List<ChartsEntity> findAllByStatus(Pageable pageable, @Param("status")
    // StatusEnum status);

    // Page<ChartsEntity> findByProfileId(Pageable pageable, @Param("profileId")
    // Integer profileId);
    @Query("SELECT p FROM StatisticEntity p where month(date) = month(now()) - 1 and year(date) = year(now()) ORDER BY p.clickCount DESC")
    Page<StatisticEntity> getTopProfileToMonth(Pageable pageable);
}

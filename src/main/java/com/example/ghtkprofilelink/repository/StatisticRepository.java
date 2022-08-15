package com.example.ghtkprofilelink.repository;

import java.util.List;

import com.example.ghtkprofilelink.model.dto.StatisticDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT new com.example.ghtkprofilelink.model.dto.StatisticDto(s.id,s.clickCount,s.profileId,s.date,p.avatarLink,p.fullname,p.shortBio) " +
            "FROM StatisticEntity s, ProfileEntity p " +
            "where p.id=s.profileId and month(date) = :month and year(date) = :year and p.status = 1" +
            "ORDER BY s.clickCount DESC")
    Page<StatisticDto> getTopProfileToMonth(Pageable pageable, @Param("month") Integer month, @Param("year") Integer year);
}

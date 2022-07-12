package com.example.ghtkprofilelink.repository;

import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.model.entity.DesignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DesignRepository extends JpaRepository<DesignEntity, Long> {
    Page<DesignEntity> findByType(Pageable pageable, @Param("type") DesignTypeEnum designType);
}

package com.example.ghtkprofilelink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ghtkprofilelink.model.entity.LinksEntity;

@Repository
public interface LinksRepository extends JpaRepository<LinksEntity, Long> {
    
}

package com.example.ghtkprofilelink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ghtkprofilelink.model.entity.LinkEntity;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
    
}

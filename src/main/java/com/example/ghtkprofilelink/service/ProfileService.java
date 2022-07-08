package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.response.Data;

import io.swagger.models.Model;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    Data getById(Long id);

    Data add(ProfileDto profileDTO, MultipartFile file);

    Data update(ProfileDto profileDTO, MultipartFile file, Long id);

    Data delete(Long id);

    Data get(HttpSession session, Long id);

    Data counter( HttpSession session, Long id); ;
}

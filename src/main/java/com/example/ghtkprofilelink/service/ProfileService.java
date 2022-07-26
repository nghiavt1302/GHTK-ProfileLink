package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;

import io.swagger.models.Model;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    Data getById(Long id);

    Data add(ProfileDto profileDTO, MultipartFile file);

    Data update(ProfileDto profileDTO, MultipartFile file, Long id);

    Data delete(Long id);

    Data getProfileByShortBio(HttpSession session,String shortBio);

    ListData getTopProfile(int page, int pageSize);

}

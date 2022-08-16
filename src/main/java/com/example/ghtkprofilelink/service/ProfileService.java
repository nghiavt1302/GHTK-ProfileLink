package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface ProfileService {
    Data getById(Long id);

    Data add(ProfileDto profileDTO, MultipartFile file);

    Data update(ProfileDto profileDTO, MultipartFile file, Long id);

    Data delete(Long id);

    Data getProfileByShortBio(HttpSession session,String shortBio);

    Data getprofileByShortBioSpam(String shortBio);

    ListData getTopProfile(int page, int pageSize);

    Data findProfileByShortBio(String shortBio);

    Data deleteProfileById(Long id);
}

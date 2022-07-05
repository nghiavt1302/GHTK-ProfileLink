package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.dto.ProfileDTO;
import com.example.ghtkprofilelink.model.response.Data;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
   Data getById(Long id);
   Data add(ProfileDTO profileDTO,MultipartFile file);
   Data update(ProfileDTO profileDTO,MultipartFile file,Long id);
   Data delete(Long id);
}

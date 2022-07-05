package com.example.ghtkprofilelink.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.model.dto.ProfileDTO;
import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    ModelMapper mapper;

    @Override
    public Data getById(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", profile);
    }

    @Override
    public Data add(ProfileDTO profileDTO,MultipartFile file) {
        ProfileEntity profile=mapper.map(profileDTO,ProfileEntity.class);
        if(!file.isEmpty()) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                profile.setAvatarUrl(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new Data(true, "success", mapper.map(profileRepository.save(profile),ProfileDTO.class));
    }

    @Override
    public Data update(ProfileDTO profileDTO,MultipartFile file, Long id) {
        ProfileEntity profile=profileRepository.findById(id).get().setValueFromDTO(profileDTO);
        profile.setId(id);
        try {
            Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            profile.setAvatarUrl(x.get("url").toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return new Data(true, "success", mapper.map(profileRepository.save(profile),ProfileDTO.class));
    }

    @Override
    public Data delete(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        profileRepository.deleteById(id);
        return new Data(true, "success", profile);
    }

}

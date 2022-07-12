package com.example.ghtkprofilelink.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.repository.ProfileRepository;

import io.swagger.models.Model;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    ModelMapper mapper;

    ProfileDto profileDto;

    @Override
    public Data getById(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success",mapper.map(profile,ProfileDto.class));
    }

    @Override
    public Data add(ProfileDto profileDto, MultipartFile file) {
        ProfileEntity profile = mapper.map(profileDto, ProfileEntity.class);
        profile.setProfileLink("localhost:8080/"+profile.getShortBio());
        if (!file.isEmpty()) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                profile.setAvatarLink(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        } else
            profile.setAvatarLink("https://res.cloudinary.com/anhtuanbui/image/upload/v1657248868/knybg0tx6rj48d62nv4a.png");

        return new Data(true, "success", mapper.map(profileRepository.save(profile), ProfileDto.class));
    }

    @Override
    public Data update(ProfileDto profileDto, MultipartFile file, Long id) {
        ProfileEntity profile = profileRepository.findById(profileDto.getId()).get().setValueFromDto(profileDto);
        profile.setId(id);
        if (!file.isEmpty()) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                profile.setAvatarLink(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new Data(true, "success", mapper.map(profileRepository.save(profile), ProfileDto.class));
    }

    @Override
    public Data delete(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        profileRepository.deleteById(id);
        return new Data(true, "success", profile);
    }

    @Override
    public Data get(HttpSession session, Long id) {
        // TODO Auto-generated method stub
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        Integer clickCount = profile.getClickCount();
        // Integer count = (Integer) session.getAttribute("count");
        Long now = new Date().getTime();
        Long firstTime = session.getCreationTime();
        Long lastTime = session.getLastAccessedTime();

        if (now == firstTime) {
            profile.setClickCount(clickCount + 1);
        } else {
            if (now >= lastTime + 3000) {
                // session.setAttribute("count", (Integer) session.getAttribute("count") + 1);
                profile.setClickCount(clickCount + 1);
            }
        }
        profileRepository.save(profile);
        return new Data(true, "success", /* session.getAttribute("count").toString() + " " + */ clickCount);
    }

    @Override
    public Data counter(HttpSession session, Long id) {
        // TODO Auto-generated method stub
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        Integer clickCount = profile.getClickCount();
        // return count.toString();
        return new Data(true, "success", clickCount);
    }

}

package com.example.ghtkprofilelink.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.entity.ChartsEntity;
import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.repository.ChartsRepository;
import com.example.ghtkprofilelink.repository.ProfileRepository;

import jdk.jshell.Snippet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ChartsRepository chartsRepository;

    @Autowired
    Cloudinary cloudinary;
    @Autowired
    ModelMapper mapper;

    ProfileDto profileDto;

    @Override
    public Data getById(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", mapper.map(profile, ProfileDto.class));
    }

    @Override
    public Data add(ProfileDto profileDto, MultipartFile file) {
        ProfileEntity profile = mapper.map(profileDto, ProfileEntity.class);
        profile.setProfileLink("localhost:8080/" + profile.getShortBio());
        if (file!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                profile.setAvatarLink(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        } else
            profile.setAvatarLink(
                    "https://res.cloudinary.com/anhtuanbui/image/upload/v1657248868/knybg0tx6rj48d62nv4a.png");

        return new Data(true, "success", mapper.map(profileRepository.save(profile), ProfileDto.class));
    }

    @Override
    public Data update(ProfileDto profileDto, MultipartFile file, Long id) {
        ProfileEntity profile = profileRepository.findById(profileDto.getId()).get().setValueFromDto(profileDto);
        profile.setId(id);
        if (file!=null) {
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
        profile.setStatus(StatusEnum.INACTIVE);
        profileRepository.save(profile);
        return new Data(true, "success", mapper.map(profile, ProfileDto.class));
    }

    @Override
    public Data getProfileByShortBio(HttpSession session, String shortBio) {
        // TODO Auto-generated method stub
        Calendar cal = Calendar.getInstance();
        ProfileEntity profile = profileRepository.getProfileByShortBio(shortBio);
        Integer profileId = profile.getId().intValue();
        List<ChartsEntity> listCharts = chartsRepository.findAllByProfileId(profileId);
        Integer counter = profile.getClickCount();
        if(listCharts.isEmpty()==true){
            counter += 1;
            profile.setClickCount(counter);
            ChartsEntity chart = new ChartsEntity();
            chart.setClickCount(1L);
            chart.setCountry(null);
            chart.setDate(new java.sql.Date(new Date().getTime()));
            chart.setProfileId(profileId.intValue());
            chartsRepository.save(chart);
        }
        else {
            ChartsEntity charts = chartsRepository.findAllByProfileId(profileId).get(chartsRepository.findAllByProfileId(profileId).size() - 1);
            Long countToMonth = charts.getClickCount();
            int monthRealTime = cal.get(Calendar.MONTH) + 1;
            Date date = charts.getDate();
            int monthDb = date.getMonth() + 1;
            Long now = new Date().getTime();
            Long lastTime = session.getLastAccessedTime();
            if ( monthRealTime >= monthDb +1){
                counter += 1;
                ChartsEntity chart = new ChartsEntity();
                chart.setClickCount(1L);
                chart.setCountry(charts.getCountry());
                chart.setDate(new java.sql.Date(new Date().getTime()));
                chart.setProfileId(profileId.intValue());
                chartsRepository.save(chart);
            }
            else {
                if (counter == null) {
                    profile.setClickCount(0);
                    charts.setClickCount(0L);
                } else {
                    if (now >= lastTime + 6000) {
                        counter += 1;
                        countToMonth += 1L;
                        profile.setClickCount(counter);
                        charts.setClickCount(countToMonth);
                    }
                }
            }
            
        } 
        profileRepository.save(profile);
        return new Data(true, "success",  mapper.map(profile, ProfileDto.class));
    }

}

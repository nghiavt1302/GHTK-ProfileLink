package com.example.ghtkprofilelink.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.entity.StatisticEntity;
import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.StatisticRepository;
import com.example.ghtkprofilelink.repository.ProfileRepository;

import com.example.ghtkprofilelink.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final StatisticRepository statisticRepository;

    private final Cloudinary cloudinary;

    private final ModelMapper mapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, StatisticRepository statisticRepository, Cloudinary cloudinary, ModelMapper mapper) {
        this.profileRepository = profileRepository;
        this.statisticRepository = statisticRepository;
        this.cloudinary = cloudinary;
        this.mapper = mapper;
    }

    @Override
    public Data getById(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", mapper.map(profile, ProfileDto.class));
    }

    @Override
    public Data add(ProfileDto profileDto, MultipartFile file) {
        ProfileEntity profile = mapper.map(profileDto, ProfileEntity.class);
        profile.setStatus(StatusEnum.ACTIVE);

        if (file != null) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                profile.setAvatarLink(x.get("url").toString());

            } catch (Exception e) {
                System.out.println(e);
            }
        } else
            profile.setAvatarLink(
                    "https://res.cloudinary.com/anhtuanbui/image/upload/v1657248868/knybg0tx6rj48d62nv4a.png");
        ProfileEntity profileEntity=profileRepository.save(profile);
        Integer profileId = profileEntity.getId().intValue();
        StatisticEntity chart = new StatisticEntity();
        chart.setClickCount(1L);
        chart.setCountry(null);
        chart.setDate(new java.sql.Date(new Date().getTime()));
        chart.setProfileId(profileId.intValue());
        statisticRepository.save(chart);
        return new Data(true, "success", mapper.map(profileEntity, ProfileDto.class));
    }

    @Override
    public Data update(ProfileDto profileDto, MultipartFile file, Long id) {
        ProfileEntity profile = profileRepository.findById(profileDto.getId()).get().setValueFromDto(profileDto);
        profile.setId(id);
        if (file != null) {
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
        Integer counter = profile.getClickCount();
        StatisticEntity charts = statisticRepository.findAllByProfileId(profileId)
                .get(statisticRepository.findAllByProfileId(profileId).size() - 1);
        Long countToMonth = charts.getClickCount();
        int monthRealTime = cal.get(Calendar.MONTH) + 1;
        Date date = charts.getDate();
        int monthDb = date.getMonth() + 1;
        Long now = new Date().getTime();
        Long lastTime = session.getLastAccessedTime();
        if (monthRealTime >= monthDb + 1) {
            counter += 1;
            StatisticEntity chart = new StatisticEntity();
            chart.setClickCount(1L);
            chart.setCountry(charts.getCountry());
            chart.setDate(new java.sql.Date(new Date().getTime()));
            chart.setProfileId(profileId.intValue());
            statisticRepository.save(chart);
        } else {
            if (counter == null) {
                profile.setClickCount(1);
                charts.setClickCount(1L);
            } else {
                if (now >= lastTime + 6000) {
                    counter += 1;
                    countToMonth += 1L;
                    profile.setClickCount(counter);
                    charts.setClickCount(countToMonth);
                }
            }
        }
        profileRepository.save(profile);
        return new Data(true, "success", mapper.map(profile, ProfileDto.class));
    }

    @Override
    public ListData getTopProfile(int page, int pageSize) {
        // TODO Auto-generated method stub
        Page<ProfileEntity> profileEntities = profileRepository.getTopProfile(PageRequest.of(page, pageSize));
        List<ProfileDto> profileDtos = profileEntities.stream().map(l -> mapper.map(l, ProfileDto.class)).collect(Collectors.toList());
        return new ListData(true, "success", profileDtos,
                new Pagination(profileEntities.getNumber(), profileEntities.getSize(), profileEntities.getTotalPages(),
                        (int) profileEntities.getTotalElements()));
    }

    

}

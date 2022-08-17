package com.example.ghtkprofilelink.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import com.example.ghtkprofilelink.model.entity.StatisticEntity;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.ProfileRepository;
import com.example.ghtkprofilelink.repository.StatisticRepository;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import com.example.ghtkprofilelink.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final StatisticRepository statisticRepository;

    private final Cloudinary cloudinary;

    private final ModelMapper mapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, StatisticRepository statisticRepository,
                              Cloudinary cloudinary, ModelMapper mapper) {
        this.profileRepository = profileRepository;
        this.statisticRepository = statisticRepository;
        this.cloudinary = cloudinary;
        this.mapper = mapper;
    }

    @Override
    public Data getById(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new Data(true, "success", mapper.map(profile, ProfileDto.class));
    }

    @Override
    public Data add(ProfileDto profileDto, MultipartFile file) {
        ProfileEntity profile = mapper.map(profileDto, ProfileEntity.class);
        profile.setStatus(StatusEnum.ACTIVE);
        profile.setShortBio(duplicateShortBioHandle(profileDto.getShortBio()));
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
        ProfileEntity profileEntity = profileRepository.save(profile);
        Integer profileId = profileEntity.getId().intValue();
        StatisticEntity chart = new StatisticEntity();
        chart.setClickCount(0L);
        chart.setDate(new java.sql.Date(new Date().getTime()));
        chart.setProfileId(profileId.intValue());
        statisticRepository.save(chart);
        return new Data(true, "success", mapper.map(profileEntity, ProfileDto.class));
    }

    public String duplicateShortBioHandle(String shortBioConverted) {
        String addInt = shortBioConverted;
        int i = 0;
        do {
            Optional<ProfileEntity> profile = profileRepository.getProfileByShortBioAndStatus(addInt, StatusEnum.ACTIVE);
            if (!profile.isPresent()) {
                return addInt;
            } else {
                addInt = shortBioConverted.concat(String.valueOf(i));
                i++;
            }
        } while (true);
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
        Optional<ProfileEntity> profile = profileRepository.getProfileByShortBioAndStatus(shortBio, StatusEnum.ACTIVE);
        if (!profile.isPresent()) return new Data(false, "profile doesn't exist", null);

        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o instanceof String)
            return new Data(true, "Someone is viewing your profile", clickCountProfile(profile.get()));

        UserEntity userEntity = ((CustomUserDetails) o).getUser();
        if (userEntity.getId().equals(profile.get().getId()))
            return new Data(true, "success your profile", mapper.map(profile.get(), ProfileDto.class));

        return new Data(true, userEntity.getUsername() + " is viewing your profile", clickCountProfile(profile.get()));
    }

    private ProfileDto clickCountProfile(ProfileEntity profile) {
        Calendar cal = Calendar.getInstance();
        Integer profileId = profile.getId().intValue();
        Integer counter = profile.getClickCount();
        StatisticEntity charts = statisticRepository.findAllByProfileId(profileId)
                .get(statisticRepository.findAllByProfileId(profileId).size() - 1);
        Long countToMonth = charts.getClickCount();
        Date date = charts.getDate();
        if (cal.get(Calendar.MONTH) + 1 >= date.getMonth() + 2) {
            counter += 1;
            profile.setClickCount(counter);
            StatisticEntity chart = new StatisticEntity();
            chart.setClickCount(1L);
            chart.setDate(new java.sql.Date(new Date().getTime()));
            chart.setProfileId(profileId.intValue());
            statisticRepository.save(chart);
        } else {
            counter += 1;
            countToMonth += 1L;
            profile.setClickCount(counter);
            charts.setClickCount(countToMonth);
        }
        return mapper.map(profileRepository.save(profile), ProfileDto.class);
    }

    @Override
    public ListData getTopProfile(int page, int pageSize) {
        // TODO Auto-generated method stub
        /// abstract
        Page<ProfileEntity> profileEntities = profileRepository.getTopProfile(PageRequest.of(page, pageSize));
        List<ProfileDto> profileDtos = profileEntities.stream().map(l -> mapper.map(l, ProfileDto.class))
                .collect(Collectors.toList());
        return new ListData(true, "success", profileDtos,
                new Pagination(profileEntities.getNumber(), profileEntities.getSize(), profileEntities.getTotalPages(),
                        (int) profileEntities.getTotalElements()));
    }

    @Override
    public Data findProfileByShortBio(String shortBio) {
        Optional<ProfileEntity> profile = profileRepository.getProfileByShortBioAndStatus(shortBio, StatusEnum.ACTIVE);
        if (profile.isPresent())
            return new Data(true, "success", profile.get());
        return new Data(false, "false", null);
    }

    @Override
    public Data deleteProfileById(Long id) {
        ProfileEntity profile = profileRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        profile.setStatus(StatusEnum.INACTIVE);
        return new Data(true, "success", mapper.map(profile, ProfileDto.class));
    }

    @Override
    public Data getprofileByShortBioSpam(String shortBio) {
        // TODO Auto-generated method stub
        ProfileEntity profile = profileRepository.getProfileByShortBioAndStatus(shortBio, StatusEnum.ACTIVE).orElseThrow(EntityNotFoundException::new);
        return new Data(true, "success", mapper.map(profile, ProfileDto.class));
    }

}

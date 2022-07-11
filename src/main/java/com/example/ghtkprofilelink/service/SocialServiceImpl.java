package com.example.ghtkprofilelink.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.SocialDto;
import com.example.ghtkprofilelink.model.entity.SocialEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.ProfileRepository;
import com.example.ghtkprofilelink.repository.SocialRepository;
import com.google.common.base.Optional;

@Service
public class SocialServiceImpl implements SocialService {

    @Autowired
    private SocialRepository socialRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public ListData getAll(int page, int pageSize) {
        Page<SocialEntity> socialEntities = socialRepository.findAll(PageRequest.of(page, pageSize));
        return new ListData(true, "success", socialEntities.getContent(),
                new Pagination(socialEntities.getNumber(), socialEntities.getSize(), socialEntities.getTotalPages(),
                        (int) socialEntities.getTotalElements()));
    }

    @Override
    public Data getById(Long id) {
        // TODO Auto-generated method stub
        SocialEntity socialEntity = socialRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", mapper.map(socialEntity, SocialDto.class));
    }

    @Override
    public Data add(SocialDto socialDto) {
        // TODO Auto-generated method stub
        SocialEntity user = mapper.map(socialDto, SocialEntity.class);
        return new Data(true, "success", mapper.map(socialRepository.save(user), SocialDto.class));
    }

    @Override
    public Data update(SocialDto socialDto, Long id) {
        // TODO Auto-generated method stub
        if (!socialRepository.existsById(socialDto.getId()))
            throw new EntityNotFoundException();
        SocialEntity socialRepo = socialRepository.getById(socialDto.getId());
        SocialEntity social = mapper.map(socialDto, SocialEntity.class);
        social.setId(socialRepo.getId());
        return new Data(true, "success", mapper.map(socialRepository.save(social), SocialDto.class));
    }

    @Override
    public Data delete(Long id) {
        // TODO Auto-generated method stub
        if (!socialRepository.existsById(id))
            throw new EntityNotFoundException();
        SocialEntity user = mapper.map(socialRepository.getById(id), SocialEntity.class);
        return new Data(true, "success", mapper.map(socialRepository.save(user), SocialDto.class));
    }

    @Override
    public ListData getByProfileId(Pageable pageable, Long profileId) {
        profileRepository.findById(profileId).orElseThrow(() -> new EntityNotFoundException());
        Page<SocialEntity> pageSocial = socialRepository.findByProfileId(pageable, profileId);
        Pagination pagination = new Pagination(pageSocial.getNumber(), pageSocial.getSize(), pageSocial.getTotalPages(),
                (int) pageSocial.getTotalElements());
        List<SocialDto> linkDtos = pageSocial.stream().map(l -> mapper.map(l, SocialDto.class)).collect(Collectors.toList());
        return new ListData(true, "success", linkDtos, pagination);
    }

}

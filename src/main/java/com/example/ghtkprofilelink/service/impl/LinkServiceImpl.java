package com.example.ghtkprofilelink.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;


import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.repository.ProfileRepository;
import com.example.ghtkprofilelink.service.LinkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.model.dto.LinkDto;
import com.example.ghtkprofilelink.model.entity.LinkEntity;
import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.LinkRepository;


@Service
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private final Cloudinary cloudinary;

    private final ModelMapper mapper;

    private final ProfileRepository profileRepository;

    public LinkServiceImpl(LinkRepository linkRepository, Cloudinary cloudinary, ModelMapper mapper, ProfileRepository profileRepository) {
        this.linkRepository = linkRepository;
        this.cloudinary = cloudinary;
        this.mapper = mapper;
        this.profileRepository = profileRepository;
    }

    @Override
    public Data getById(Long id) {
        LinkEntity link = linkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", mapper.map(link, LinkDto.class));
    }

    @Override
    public ListData getByProfileId(Pageable pageable, Long profileId) {
        profileRepository.findById(profileId).orElseThrow(() -> new EntityNotFoundException());
        Page<LinkEntity> pageLink = linkRepository.findByProfileId(pageable, profileId);
        Pagination pagination = new Pagination(pageLink.getNumber(), pageLink.getSize(), pageLink.getTotalPages(), (int) pageLink.getTotalElements());
        List<LinkDto> linkDtos = pageLink.stream().map(l -> mapper.map(l, LinkDto.class)).collect(Collectors.toList());
        return new ListData(true, "success", linkDtos, pagination);
    }

    @Override
    public Data add(LinkDto linkDTO, MultipartFile file) {
        LinkEntity link = mapper.map(linkDTO, LinkEntity.class);
        if (file!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                link.setPicture(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new Data(true, "success", mapper.map(linkRepository.save(link), LinkDto.class));
    }

    @Override
    public Data update(LinkDto linkDTO, MultipartFile file, Long id) {
        // TODO Auto-generated method stub
        LinkEntity link = linkRepository.findById(linkDTO.getId()).get().setValueFromDto(linkDTO);
        link.setId(id);
        if (file!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                link.setPicture(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new Data(true, "success", mapper.map(linkRepository.save(link), LinkDto.class));
    }

    @Override
    public Data delete(Long id) {
        // TODO Auto-generated method stub
        LinkEntity link = linkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        linkRepository.deleteById(id);
        return new Data(true, "success", mapper.map(link, LinkDto.class));
    }



}

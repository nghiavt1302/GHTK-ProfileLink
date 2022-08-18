package com.example.ghtkprofilelink.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.constants.BackgroundTypeEnum;
import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.example.ghtkprofilelink.model.entity.DesignEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.DesignRepository;
import com.example.ghtkprofilelink.service.DesignService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import  java.util.Optional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DesignServiceImpl implements DesignService {

    private final DesignRepository designRepository;

    private final Cloudinary cloudinary;

    private final ModelMapper modelMapper;

    public DesignServiceImpl(DesignRepository designRepository, Cloudinary cloudinary, ModelMapper modelMapper) {
        this.designRepository = designRepository;
        this.cloudinary = cloudinary;
        this.modelMapper = modelMapper;
    }

    @Override
    public Data getById(Long id) {
        DesignEntity design = designRepository.findByIdAndStatus(id,StatusEnum.ACTIVE).orElseThrow(EntityNotFoundException::new);
        return new Data(true, "success", modelMapper.map(design, DesignDto.class));
    }

    @Override
    public ListData getListDesignByType(Pageable pageable, DesignTypeEnum designType, StatusEnum status, String name) {
        Page<DesignEntity> pageDesign = designRepository.findByTypeAndStatusAndNameContaining(pageable, designType , status, name);
        Pagination pagination = new Pagination(pageDesign.getNumber(), pageDesign.getSize(), pageDesign.getTotalPages(), (int) pageDesign.getTotalElements());
        List<DesignDto> listDesignDto = pageDesign.stream().map(d -> modelMapper.map(d, DesignDto.class)).collect(Collectors.toList());
        return new ListData(true, "success", listDesignDto, pagination);
    }

    @Override
    public Data add(DesignDto designDto, MultipartFile avatar,MultipartFile backgroundImage) {
        DesignEntity designEntity = modelMapper.map(designDto, DesignEntity.class);
        designEntity.setStatus(StatusEnum.ACTIVE);

        if (!designEntity.getBackgroundColor().equals("")) {
            designEntity.setBackgroundType(BackgroundTypeEnum.COLOR);
        }

        if (avatar!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                designEntity.setPicture(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (backgroundImage!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(backgroundImage.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                designEntity.setBackgroundImage(x.get("url").toString());
                designEntity.setBackgroundType(BackgroundTypeEnum.IMAGE);
            } catch (Exception e) {
                System.out.println(e);
            }
        }


        return new Data(true, "success", modelMapper.map(designRepository.save(designEntity), DesignDto.class));
    }

    @Override
    public Data update(DesignDto designDto, MultipartFile avatar,MultipartFile backgroundImage, Long id) {
        DesignEntity designEntity = designRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        designEntity = designEntity.setValueByDto(designDto);
        designEntity.setId(id);
        if (designEntity.getBackgroundColor().equals("")) {
            designEntity.setBackgroundType(BackgroundTypeEnum.COLOR);
        }

        if (avatar!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                designEntity.setPicture(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (backgroundImage!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(backgroundImage.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                designEntity.setBackgroundImage(x.get("url").toString());
                designEntity.setBackgroundType(BackgroundTypeEnum.IMAGE);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new Data(true, "success", modelMapper.map(designRepository.save(designEntity), DesignDto.class));
    }

    @Override
    public Data delete(Long id) {
        DesignEntity designEntity = designRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        designEntity.setStatus(StatusEnum.INACTIVE);
        designRepository.save(designEntity);

        return new Data(true, "success", modelMapper.map(designEntity, DesignDto.class));
    }

    @Override
    public Data deleteListDesign(List<DesignDto> listDesign){
        List<DesignDto> designs=new ArrayList<>();
        listDesign.forEach(design->{
            designs.add((DesignDto) delete(design.getId()).getData());
        });
        return new Data(true, "success", designs);
    }

    @Override
    public Data findByName(String name){
        Optional<DesignEntity> designEntity = designRepository.findByName(name);
        if(designEntity.isPresent()){
            return new Data(true, "success", modelMapper.map(designEntity.get(), DesignDto.class));
        } else return new Data(true, "success", null);
    }
}

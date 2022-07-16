package com.example.ghtkprofilelink.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.constants.BackgroundTypeEnum;
import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.example.ghtkprofilelink.model.entity.DesignEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.DesignRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DesignServiceImpl implements DesignService {
    @Autowired
    DesignRepository designRepository;
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Data getById(Long id) {
        DesignEntity design = designRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", modelMapper.map(design, DesignDto.class));
    }

    @Override
    public ListData getListDesignByType(Pageable pageable, DesignTypeEnum designType) {
        Page<DesignEntity> pageDesign = designRepository.findByType(pageable, designType);
        Pagination pagination = new Pagination(pageDesign.getNumber(), pageDesign.getSize(), pageDesign.getTotalPages(), (int) pageDesign.getTotalElements());
        List<DesignDto> listDesignDto = pageDesign.stream().map(d -> modelMapper.map(d, DesignDto.class)).collect(Collectors.toList());
        return new ListData(true, "success", listDesignDto, pagination);
    }

    @Override
    public Data add(DesignDto designDto, MultipartFile file) {
        DesignEntity designEntity = modelMapper.map(designDto, DesignEntity.class);
        if (designEntity.getBackgroundColor().equals("")) {
            designEntity.setBackgroundType(BackgroundTypeEnum.COLOR);
        }
        if (file!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                designEntity.setBackgroundImage(x.get("url").toString());
                designEntity.setBackgroundType(BackgroundTypeEnum.IMAGE);
            } catch (Exception e) {
                System.out.println(e);
            }
        }


        return new Data(true, "success", modelMapper.map(designRepository.save(designEntity), DesignDto.class));
    }

    @Override
    public Data update(DesignDto designDto, MultipartFile file, Long id) {
        DesignEntity designEntity = designRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        designEntity = designEntity.setValueByDto(designDto);
        designEntity.setId(id);
        if (designEntity.getBackgroundColor().equals("")) {
            designEntity.setBackgroundType(BackgroundTypeEnum.COLOR);
        }
        if (file!=null) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
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
        DesignEntity designEntity = designRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        designRepository.deleteById(id);

        return new Data(true, "success", modelMapper.map(designEntity, DesignDto.class));
    }
}

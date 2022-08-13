package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DesignService {
    Data getById(Long id);

    ListData getListDesignByType(Pageable pageable, DesignTypeEnum typeDesign, StatusEnum status, String name);

    Data add(DesignDto designDto, MultipartFile avatar, MultipartFile backgroundImage);

    Data update(DesignDto designDto, MultipartFile avatar, MultipartFile backgroundImage, Long id);

    Data delete(Long id);

    Data deleteListDesign(List<DesignDto> listDesign);

    Data findByName(String name);
}

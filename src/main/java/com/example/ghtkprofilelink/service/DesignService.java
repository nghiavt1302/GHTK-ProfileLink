package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface DesignService {
    Data getById(Long id);

    ListData getListDesignByType(Pageable pageable, DesignTypeEnum typeDesign);

    Data add(DesignDto designDto, MultipartFile file);

    Data update(DesignDto designDto, MultipartFile file, Long id);

    Data delete(Long id);
}

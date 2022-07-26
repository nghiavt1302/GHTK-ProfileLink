package com.example.ghtkprofilelink.service;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

import com.example.ghtkprofilelink.model.dto.ChartsDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;

import antlr.collections.List;

public interface ChartsService {

    ListData getAll(int page, int pageSize);

    Data getById(Long id);

    Data add(ChartsDto chartsDto);

    Data update(ChartsDto chartsDto,Long id);

    Data delete(Long id);

    // ListData getByProfileId(Pageable pageable,Integer profileId);
    ListData getTopProfileToMonth(int page, int pageSize);
}

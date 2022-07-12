package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.dto.ChartsDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;

public interface ChartsService {

    ListData getAll(int page, int pageSize);

    Data getById(Long id);

    Data add(ChartsDto chartsDto);

    Data update(ChartsDto chartsDto,Long id);

    Data delete(Long id);
}

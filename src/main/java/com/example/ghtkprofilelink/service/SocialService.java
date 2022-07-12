package com.example.ghtkprofilelink.service;

import org.springframework.stereotype.Service;

import com.example.ghtkprofilelink.model.dto.SocialDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;

@Service
public interface SocialService {
    ListData getAll(int page, int pageSize);

    Data getById(Long id);

    Data add(SocialDto socialDto);

    Data update(SocialDto socialDto);

    Data delete(Long id);

}

package com.example.ghtkprofilelink.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.ghtkprofilelink.model.dto.LinksDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.Pagination;

public interface LinksService {
    Data getById(Long id);

    Data add(LinksDto linkDTO, MultipartFile file);

    Data update(LinksDto linkDTO, MultipartFile file, Long id);

    Data delete(Long id);

    ResponseEntity<Data> getAll();
}

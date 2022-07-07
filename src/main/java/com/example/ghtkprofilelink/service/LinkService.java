package com.example.ghtkprofilelink.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.ghtkprofilelink.model.dto.LinkDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.Pagination;

public interface LinkService {
    Data getById(Long id);

    Data add(LinkDto linkDTO, MultipartFile file);

    Data update(LinkDto linkDTO, MultipartFile file, Long id);

    Data delete(Long id);

    ResponseEntity<Data> getAll();
}

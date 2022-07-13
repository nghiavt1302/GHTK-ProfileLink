package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.example.ghtkprofilelink.service.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/design")
public class DesignController {
    @Autowired
    DesignService designService;

    @GetMapping("")
    public ResponseEntity<?> getListDesignDefault(
            @RequestParam("page") int page,
            @RequestParam("page_size") int pageSize) {
        return new ResponseEntity<>(
                designService.getListDesignByType(PageRequest.of(page, pageSize), DesignTypeEnum.DEFAULT),
                HttpStatus.valueOf(200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.getById(id), HttpStatus.valueOf(200));
    }

    @PostMapping("")
    public ResponseEntity<?> add(
            @ModelAttribute DesignDto designDto,
            @RequestParam(required = false) MultipartFile file) {
        return new ResponseEntity<>(designService.add(designDto, file), HttpStatus.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestBody DesignDto designDto,
            @RequestParam(required = false) MultipartFile file,
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.update(designDto, file, id), HttpStatus.valueOf(201));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.delete(id), HttpStatus.valueOf(200));
    }
}

package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.example.ghtkprofilelink.service.DesignServiceImpl;
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
    DesignServiceImpl designService;

    @GetMapping("")
    public ResponseEntity<?> getListDesignDefault(
            @RequestParam("page") int page,
            @RequestParam("page_size") int pageSize) {
        return new ResponseEntity<>(
                designService.getListDesignByType(PageRequest.of(page, pageSize), DesignTypeEnum.DEFAULT, StatusEnum.ACTIVE),
                HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.getById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> add(
            @ModelAttribute DesignDto designDto,
            @RequestParam(required = false) MultipartFile file) {
        return new ResponseEntity<>(designService.add(designDto, file), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestBody DesignDto designDto,
            @RequestParam(required = false) MultipartFile file,
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.update(designDto, file, id), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.delete(id), HttpStatus.OK);
    }

    @GetMapping("/get-by-name/{name}")
    public ResponseEntity<?> getByName(
            @PathVariable String name
    ){
        return new ResponseEntity<>(designService.findByName(name),HttpStatus.OK);
    }
}

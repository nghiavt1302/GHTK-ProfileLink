package com.example.ghtkprofilelink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ghtkprofilelink.model.dto.LinkDto;
import com.example.ghtkprofilelink.service.LinkService;


@RestController
@RequestMapping("api/v1.0/link")
public class LinkController {
    @Autowired
    LinkService linkService;

    @GetMapping("")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(linkService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(linkService.getById(id), HttpStatus.valueOf(200));
    }

    @GetMapping("/list/{profileId}")
    public ResponseEntity<?> getByProfileId(
            @RequestParam("page") int page,
            @RequestParam("page_size") int pageSize,
            @PathVariable Long profileId
    ){
        return new ResponseEntity<>(linkService.getByProfileId(PageRequest.of(page,pageSize),profileId),HttpStatus.valueOf(200));
    }
    @PostMapping("")
    public ResponseEntity<?> add(@ModelAttribute LinkDto linkDto, @RequestParam MultipartFile file) {
        return new ResponseEntity<>(linkService.add(linkDto, file), HttpStatus.valueOf(201));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @ModelAttribute LinkDto linkDto,
            @RequestParam MultipartFile file,
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(linkService.update(linkDto, file, id), HttpStatus.valueOf(201));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(linkService.delete(id), HttpStatus.valueOf(200));
    }
}
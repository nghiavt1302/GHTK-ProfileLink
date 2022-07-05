package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.ProfileDTO;
import com.example.ghtkprofilelink.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1.0/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(profileService.getById(id),HttpStatus.valueOf(200));
    }

    @PostMapping("")
    public ResponseEntity<?> add(@ModelAttribute ProfileDTO profileDTO, @RequestParam MultipartFile file){
        return new ResponseEntity<>(profileService.add(profileDTO,file), HttpStatus.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @ModelAttribute ProfileDTO profileDTO,
            @RequestParam MultipartFile file,
            @PathVariable Long id
    ){
        return new ResponseEntity<>(profileService.update(profileDTO,file,id),HttpStatus.valueOf(201));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(profileService.delete(id),HttpStatus.valueOf(200));
    }
}

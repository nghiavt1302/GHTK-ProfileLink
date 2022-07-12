package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.service.ProfileService;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1.0/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

   
    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id) {
        return new ResponseEntity<>(profileService.getById(id), HttpStatus.valueOf(200));
    }

    @PostMapping("")
    public ResponseEntity<?> add(@ModelAttribute ProfileDto profileDto, @RequestParam(required = false) MultipartFile file) {
        return new ResponseEntity<>(profileService.add(profileDto, file), HttpStatus.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @ModelAttribute ProfileDto profileDto,
            @RequestParam MultipartFile file,
            @PathVariable Long id) {
        return new ResponseEntity<>(profileService.update(profileDto, file, id), HttpStatus.valueOf(201));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {

        return new ResponseEntity<>(profileService.delete(id), HttpStatus.valueOf(200));
    }

    @GetMapping("/getbyshortbio")
    public ResponseEntity<?> getByShortBio(HttpSession session,@RequestParam String shortBio) {
        return ResponseEntity.ok(profileService.getProfileByShortBio(session, shortBio));
    }
}

package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.service.ProfileService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/profile")
public class ProfileController {

    private final Bucket bucket;

    @Autowired
    ProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id) {
        return new ResponseEntity<>(profileService.getById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> add(@ModelAttribute ProfileDto profileDto,
            @RequestParam(required = false) MultipartFile file) {
        return new ResponseEntity<>(profileService.add(profileDto, file), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @ModelAttribute ProfileDto profileDto,
            @RequestParam(required = false) MultipartFile file,
            @PathVariable Long id) {
        return new ResponseEntity<>(profileService.update(profileDto, file, id), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {

        return new ResponseEntity<>(profileService.delete(id), HttpStatus.OK);
    }

    // hàm dùng để giới hạn số lượng requests
    public ProfileController() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/shortbio")
    public ResponseEntity<?> getByShortBio(HttpSession session, @RequestParam String shortBio) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(profileService.getProfileByShortBio(session, shortBio));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopProfile(@RequestParam("page") int page, @RequestParam("page_size") int pageSize) {
        return new ResponseEntity<>(profileService.getTopProfile(page, pageSize),HttpStatus.OK);
    }

}

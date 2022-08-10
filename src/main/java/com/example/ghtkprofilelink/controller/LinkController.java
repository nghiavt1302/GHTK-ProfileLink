package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.LinkDto;
import com.example.ghtkprofilelink.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/link")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(linkService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/list/{profileId}")
    public ResponseEntity<?> getByProfileId(
            @RequestParam("page") int page,
            @RequestParam("page-size") int pageSize,
            @PathVariable Long profileId
    ){
        return new ResponseEntity<>(linkService.getByProfileId(PageRequest.of(page,pageSize),profileId),HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> add(@ModelAttribute LinkDto linkDto, @RequestParam(required = false) MultipartFile file) {
        return new ResponseEntity<>(linkService.add(linkDto, file), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @ModelAttribute LinkDto linkDto,
            @RequestParam(required = false) MultipartFile file,
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(linkService.update(linkDto, file, id), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(linkService.delete(id), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestBody MultipartFile file){
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}

package com.example.ghtkprofilelink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ghtkprofilelink.model.dto.SocialDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.service.SocialServiceImpl;

@RestController
@RequestMapping("api/v1.0/social")
public class SocialController {

    @Autowired
    SocialServiceImpl socialService;
    @GetMapping()
    public ResponseEntity<ListData> getAll(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(socialService.getAll(page, pageSize));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Data> getById(@PathVariable Long id) {
        return ResponseEntity.ok(socialService.getById(id));
    }
    @PostMapping("add")
    public ResponseEntity<Data> add(@RequestBody SocialDto socialDto) {
        return ResponseEntity.ok(socialService.add(socialDto));
    }
    @PutMapping()
    public ResponseEntity<Data> update(@RequestBody SocialDto socialDto, @PathVariable Long id) {
        return ResponseEntity.ok(socialService.update(socialDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Data> delete(@PathVariable Long id) {
        return ResponseEntity.ok(socialService.delete(id));
    }

    @GetMapping("/social/{profileId}")
    public ResponseEntity<?> getByProfileId(
            @RequestParam("page") int page,
            @RequestParam("page_size") int pageSize,
            @PathVariable Long profileId
    ){
        return new ResponseEntity<>(socialService.getByProfileId(PageRequest.of(page,pageSize),profileId),HttpStatus.valueOf(200));
    }
}

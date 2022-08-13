package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.DesignDto;
import com.example.ghtkprofilelink.service.impl.DesignServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/design")
public class DesignController {

    private final DesignServiceImpl designService;

    public DesignController(DesignServiceImpl designService) {
        this.designService = designService;
    }

    @GetMapping("")
    public ResponseEntity<?> getListDesignDefault(
            @RequestParam("name") String name,
            @RequestParam("page") int page,
            @RequestParam("page-size") int pageSize) {
        return new ResponseEntity<>(
                designService.getListDesignByType(PageRequest.of(page, pageSize), DesignTypeEnum.DEFAULT, StatusEnum.ACTIVE, name),
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
            @RequestParam(name="avatar",required = false) MultipartFile avatar,
            @RequestParam(name="background-image",required = false) MultipartFile backgroundImage) {
        return new ResponseEntity<>(designService.add(designDto,avatar, backgroundImage), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @ModelAttribute DesignDto designDto,
            @RequestParam(name="avatar",required = false) MultipartFile avatar,
            @RequestParam(name="background-image",required = false) MultipartFile backgroundImage,
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.update(designDto, avatar, backgroundImage, id), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {
        return new ResponseEntity<>(designService.delete(id), HttpStatus.OK);
    }

    @PostMapping("/delete/list")
    public ResponseEntity<?> deleteListDesign(@RequestBody List<DesignDto> listDesign){
        return new ResponseEntity<>(designService.deleteListDesign(listDesign),HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByName(
            @PathVariable String name
    ){
        return new ResponseEntity<>(designService.findByName(name),HttpStatus.OK);
    }
}

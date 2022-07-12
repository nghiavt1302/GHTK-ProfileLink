package com.example.ghtkprofilelink.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import com.example.ghtkprofilelink.constants.DesignTypeEnum;
import com.example.ghtkprofilelink.model.dto.ChartsDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.service.ChartsServiceImpl;

@RestController
@RequestMapping("api/v1.0/charts")
public class ChartsController {
    @Autowired
    ChartsServiceImpl chartsService;

    @GetMapping()
    public ResponseEntity<ListData> getAll(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(chartsService.getAll(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data> getById(@PathVariable Long id) {
        return ResponseEntity.ok(chartsService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Data> add(@RequestBody ChartsDto chartsDto) {
        return ResponseEntity.ok(chartsService.add(chartsDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Data> update(@RequestBody ChartsDto chartsDto, @PathVariable Long id) {
        return ResponseEntity.ok(chartsService.update(chartsDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Data> delete(@PathVariable Long id) {
        return ResponseEntity.ok(chartsService.delete(id));
    }

    // @GetMapping("/getbyprofileid")
    // public ResponseEntity<ListData> getByProfileId(@RequestParam("page") int page,
    //         @RequestParam("page_size") int pageSize, @PathVariable Integer profileId) {
    //     return ResponseEntity.ok(chartsService.getByProfileId(null, profileId));
    // }
}

package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.ChartsDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.service.ChartsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
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

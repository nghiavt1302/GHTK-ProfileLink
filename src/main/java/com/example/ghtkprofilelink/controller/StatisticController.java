package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.StatisticDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.service.impl.StatisticServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/statistic")
public class StatisticController {

    StatisticServiceImpl chartsService;

    public StatisticController(StatisticServiceImpl chartsService) {
        this.chartsService = chartsService;
    }

    @GetMapping()
    public ResponseEntity<ListData> getAll(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(chartsService.getAll(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data> getById(@PathVariable Long id) {
        return ResponseEntity.ok(chartsService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Data> add(@RequestBody StatisticDto statisticDto) {
        return ResponseEntity.ok(chartsService.add(statisticDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Data> update(@RequestBody StatisticDto statisticDto, @PathVariable Long id) {
        return ResponseEntity.ok(chartsService.update(statisticDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Data> delete(@PathVariable Long id) {
        return ResponseEntity.ok(chartsService.delete(id));
    }
    
    @GetMapping("/top-month")
    public ResponseEntity<?> getTopProfileToMonth(@RequestParam("page") int page, @RequestParam("page-size") int pageSize,@RequestParam("month") int month, @RequestParam("year") int year) {
        return new ResponseEntity<>(chartsService.getTopProfileToMonth(page, pageSize, month, year), HttpStatus.OK);
    }

    // @GetMapping("/getbyprofileid")
    // public ResponseEntity<ListData> getByProfileId(@RequestParam("page") int page,
    //         @RequestParam("page_size") int pageSize, @PathVariable Integer profileId) {
    //     return ResponseEntity.ok(chartsService.getByProfileId(null, profileId));
    // }
}

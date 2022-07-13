package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping()
    public ResponseEntity<ListData> getAll(@RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(userService.getAll(page, pageSize));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Data> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.getById(id));
//    }

    @GetMapping("/username")
    public ResponseEntity<Data> getByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @PostMapping("add")
    public ResponseEntity<Data> add(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.add(userDto));
    }

    @PutMapping()
    public ResponseEntity<Data> update(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(userDto));
    }

    @DeleteMapping("")
    public ResponseEntity<Data> deleteByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.deleteByUsername(username));
    }

    // Thymeleaf test login FB
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}

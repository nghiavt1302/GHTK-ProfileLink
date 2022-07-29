package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

//    @GetMapping()
//    public ResponseEntity<?> getAll(@RequestParam int page, @RequestParam int pageSize) {
//        return  new ResponseEntity<>(userService.getAll(page, pageSize), HttpStatus.valueOf(200));
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Data> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.getById(id));
//    }

    @GetMapping("/username")
    public ResponseEntity<?> getByUsername(@RequestParam String username) {
        return new ResponseEntity<>(userService.getByUsername(username) ,HttpStatus.valueOf(200));
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.add(userDto), HttpStatus.valueOf(200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserDto userDto,@PathVariable Long id) {
        return new ResponseEntity<>(userService.update(userDto,id), HttpStatus.valueOf(200));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteByUsername(@RequestParam String username) {
        return new ResponseEntity<>(userService.deleteByUsername(username), HttpStatus.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.valueOf(200));
    }
}

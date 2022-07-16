package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import com.example.ghtkprofilelink.security.jwt.JwtTokenProvider;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/test/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserDto user) {
        // Xác thực thông tin người dùng Request lên
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new ResponseEntity<>(new Data(true, "success", "Bearer " + jwt), HttpStatus.valueOf(200));
    }

    @PostMapping("/test/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegister user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        return new ResponseEntity<>(userService.register(user, request.getRequestURL().toString()),HttpStatus.valueOf(200));
    }

    @GetMapping("/test/register/verify")
    public ResponseEntity<?> verifyUser(@Param("code") String code) {
        return new ResponseEntity<>(userService.verify(code),HttpStatus.valueOf(200));
    }

    @GetMapping("/update_password_token")
    public ResponseEntity<Data> updatePasswordToken(@RequestParam String mail, HttpServletRequest request) throws MessagingException {
        return ResponseEntity.ok(userService.updatePasswordToken(mail, request.getRequestURL().append("/?code=")));
    }

    @PostMapping("/update_password_token")
    public ResponseEntity<Data> updatePassword(@Param("code") String code, @RequestParam String password) {
        return ResponseEntity.ok(userService.updatePassword(code, password));
    }
}

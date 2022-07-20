package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.example.ghtkprofilelink.model.playload.LoginResponse;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import com.example.ghtkprofilelink.security.jwt.JwtTokenProvider;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    public ResponseEntity<Data> authenticateUser(@Valid @RequestBody UserDto user) {
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
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(new Data(true, "success", new LoginResponse("Bearer " + jwt, userDetails.getUser())));
    }

    @PostMapping("/test/register")
    public ResponseEntity<Data> registerUser(@Valid @RequestBody UserRegister user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(userService.register(user, request.getRequestURL().append("/verify?code=")));
    }

    @GetMapping("/test/register/verify")
    public ResponseEntity<Data> verifyUser(@Param("code") String code) {
        return ResponseEntity.ok(userService.verify(code));
    }

    @GetMapping("/update_password_token")
    public ResponseEntity<Data> updatePasswordToken(@RequestParam String mail, HttpServletRequest request) throws MessagingException {
        return ResponseEntity.ok(userService.updatePasswordToken(mail, request.getRequestURL().append("/?code=")));
    }

    @PostMapping("/update_password_token")
    public ResponseEntity<Data> updatePassword(@Param("code") String code, @RequestParam String password) {
        return ResponseEntity.ok(userService.updatePassword(code, password));
    }

    @GetMapping("/test/forgot_password")
    public ResponseEntity<Data> forgotPassword(@RequestParam String mail) throws MessagingException {
        return ResponseEntity.ok(userService.forgotPassword(mail));
    }
}

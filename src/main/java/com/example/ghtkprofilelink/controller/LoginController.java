package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.example.ghtkprofilelink.model.playload.LoginResponse;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import com.example.ghtkprofilelink.security.jwt.JwtTokenProvider;
import com.example.ghtkprofilelink.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
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

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserServiceImpl userService;

    private final ModelMapper mapper;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserServiceImpl userService, ModelMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.mapper = mapper;
    }

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
        return ResponseEntity.ok(new Data(true, "success", new LoginResponse("Bearer " + jwt,mapper.map(userDetails.getUser(),UserDto.class))));
    }

    @PostMapping("/test/register")
    public ResponseEntity<Data> registerUser(@Valid @RequestBody UserRegister user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(userService.register(user, new StringBuffer("http://localhost:4200/register-verify/")));
    }

    @GetMapping("/test/register/verify")
    public ResponseEntity<Data> verifyUser(@RequestParam("code") String code) {
        return ResponseEntity.ok(userService.verify(code));
    }

    @GetMapping("/update-password-token")
    public ResponseEntity<Data> updatePasswordToken(@RequestParam String mail) throws MessagingException {
        return ResponseEntity.ok(userService.updatePasswordToken(mail, new StringBuffer("http://localhost:4200/update_password_token?code=")));
    }

    @PostMapping("/test/update-password-token")
    public ResponseEntity<Data> updatePassword(@RequestParam String code, @RequestParam String password) {
        return ResponseEntity.ok(userService.updatePassword(code, password));
    }

    @GetMapping("/test/forgot-password")
    public ResponseEntity<Data> forgotPassword(@RequestParam String mail) throws MessagingException {
        return ResponseEntity.ok(userService.forgotPassword(mail));
    }
}

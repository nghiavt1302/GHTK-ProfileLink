package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.security.jwt.JwtTokenProvider;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginFBController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserServiceImpl userService;
    // Thymeleaf test login FB
    @GetMapping("/loginFb")
    public String loginPage(){
        return "loginFB";
    }
}

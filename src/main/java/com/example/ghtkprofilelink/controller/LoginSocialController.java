package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.constants.ProviderEnum;
import com.example.ghtkprofilelink.model.dto.TokenDto;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.playload.LoginResponse;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import com.example.ghtkprofilelink.security.jwt.JwtTokenProvider;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@CrossOrigin
@RestController
@RequestMapping("/login/oauth")
public class LoginSocialController {
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    String googleClientId;

    @Value("${jwt.secret}")
    String secretPsw;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired

    private JwtTokenProvider tokenProvider;

    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody TokenDto tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                        .setAudience(Collections.singletonList(googleClientId));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        UserEntity userEntity = new UserEntity();
        TokenDto tokenRes = addToken(userService.processOAuthPostLogin(userEntity, ProviderEnum.GOOGLE));
        return new ResponseEntity<>(new Data(true, "success", new LoginResponse("Bearer " + tokenRes.getValue(), userEntity)),HttpStatus.OK);

    }

    @PostMapping("/facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDto tokenDto) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String [] fields = {"email", "picture"};
        User user = facebook.fetchObject("me", User.class, fields);
        UserEntity userEntity = new UserEntity();

        TokenDto tokenRes = addToken(userService.processOAuthPostLogin(userEntity,ProviderEnum.FACEBOOK));
        return new ResponseEntity<>(new Data(true, "success", new LoginResponse("Bearer " + tokenRes.getValue(), userEntity)),HttpStatus.OK);

    }

    public TokenDto addToken(UserEntity user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getMail(),
                        user.getUsername()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userDetails);
        return new TokenDto(jwt);
    }

    // Thymeleaf test login FB
//    @GetMapping("/loginFb")
//    public String loginPage(){
//        return "loginFB";
//    }


}

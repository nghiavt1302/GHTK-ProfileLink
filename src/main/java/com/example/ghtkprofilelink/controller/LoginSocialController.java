package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.constants.ProviderEnum;
import com.example.ghtkprofilelink.model.dto.TokenDto;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.playload.LoginResponse;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import com.example.ghtkprofilelink.security.jwt.JwtTokenProvider;
import com.example.ghtkprofilelink.service.impl.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.api.client.json.jackson2.JacksonFactory;

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

    private final UserServiceImpl userService;

    private final JwtTokenProvider tokenProvider;

    private final ModelMapper mapper;

    public LoginSocialController(UserServiceImpl userService, JwtTokenProvider tokenProvider, ModelMapper mapper) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.mapper = mapper;
    }

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
        userEntity.setMail(payload.getEmail());
        userEntity.setUsername(payload.getEmail());
        userEntity = userService.processOAuthPostLogin(userEntity, ProviderEnum.GOOGLE);
        TokenDto tokenRes = addToken(userEntity);
        return new ResponseEntity<>(new Data(true, "success", new LoginResponse("Bearer " + tokenRes.getValue(), mapper.map(userEntity, UserDto.class))),HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDto tokenDto) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String [] fields = {"email","name"};
        User user = facebook.fetchObject("me", User.class, fields);
        UserEntity userEntity = new UserEntity();
        userEntity.setMail(user.getEmail());
        userEntity.setUsername(user.getName());
        userEntity=userService.processOAuthPostLogin(userEntity,ProviderEnum.FACEBOOK);
        TokenDto tokenRes = addToken(userEntity);
        return new ResponseEntity<>(new Data(true, "success", new LoginResponse("Bearer " + tokenRes.getValue(), mapper.map(userEntity,UserDto.class))),HttpStatus.OK);

    }

    public TokenDto addToken(UserEntity user){
        CustomUserDetails userDetails=new CustomUserDetails(user);
        String jwt = tokenProvider.generateToken(userDetails);
        return new TokenDto(jwt);
    }

    // Thymeleaf test login FB
//    @GetMapping("/loginFb")
//    public String loginPage(){
//        return "loginFB";
//    }


}

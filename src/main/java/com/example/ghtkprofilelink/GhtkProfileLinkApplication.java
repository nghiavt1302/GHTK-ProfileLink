package com.example.ghtkprofilelink;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class GhtkProfileLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhtkProfileLinkApplication.class, args);
    }

}

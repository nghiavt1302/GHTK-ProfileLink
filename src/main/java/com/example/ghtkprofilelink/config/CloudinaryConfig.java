package com.example.ghtkprofilelink.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "anhtuanbui",
                "api_key", "139131292336246",
                "api_secret", "UVmBForKDw6q49bglU26ZTKyG7U",
                "secure", true
        ));
        return c;
    }
}

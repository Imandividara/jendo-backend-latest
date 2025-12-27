package com.jendo.app.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dlmitfrfe",
            "api_key", "873988854946148",
            "api_secret", "Oq8BXQpr0bFeGHru792ojvnU5-M"
        ));
    }
}

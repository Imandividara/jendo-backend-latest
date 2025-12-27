package com.jendo.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // Serve uploaded files from the uploads directory
            // Use absolute path to ensure files are served correctly
            Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
            
            // Create directories if they don't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created uploads directory at: {}", uploadPath);
            }
            
            // Create profile-images subdirectory
            Path profileImagesPath = uploadPath.resolve("profile-images");
            if (!Files.exists(profileImagesPath)) {
                Files.createDirectories(profileImagesPath);
                logger.info("Created profile-images directory at: {}", profileImagesPath);
            }
            
            // Create report-attachments subdirectory
            Path reportAttachmentsPath = uploadPath.resolve("report-attachments");
            if (!Files.exists(reportAttachmentsPath)) {
                Files.createDirectories(reportAttachmentsPath);
                logger.info("Created report-attachments directory at: {}", reportAttachmentsPath);
            }
            
            String uploadLocation = "file:" + uploadPath.toString() + File.separator;
            logger.info("Serving static files from: {}", uploadLocation);
            logger.info("Files accessible at: /uploads/**");
            
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations(uploadLocation)
                    .setCachePeriod(3600);
                    
        } catch (IOException e) {
            logger.error("Failed to create upload directories", e);
        }
    }
}

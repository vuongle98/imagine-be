package com.vuongle;

import com.vuongle.imagine.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties(StorageProperties.class)
public class ImagineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagineApplication.class, args);
    }

}

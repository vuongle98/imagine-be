package com.vuongle;

import com.vuongle.imagine.properties.StorageProperties;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties(StorageProperties.class)
@OpenAPIDefinition(
        info = @Info(
                title = "My app REST APIs",
                description = "My app REST APIs Documentation",
                version = "1.0.0",
                contact = @Contact(
                        email = "vvuong1998@gmail.com",
                        name = "Vuong Le"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "My app documentation"
        )
)
public class ImagineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagineApplication.class, args);
    }

}

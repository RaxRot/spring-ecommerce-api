package com.raxrot.sproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//http://localhost:8080/swagger-ui/index.html#/
@OpenAPIDefinition(
        info = @Info(
                title = "SB E-Commerce API",
                version = "1.0",
                description = "Spring Boot E-Commerce REST API",
                contact = @Contact(
                        name = "RaxRot ❤️",
                        email = "dasistperfektosss@gmail.com",
                        url = "https://github.com/RaxRot"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server")
        }
)
@SpringBootApplication
public class SbEcomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbEcomApplication.class, args);
    }

}

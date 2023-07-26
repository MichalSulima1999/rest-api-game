package com.michael1099.rest_rpg;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/api", description = "Default Server URL")
        }
)
@SpringBootApplication
public class RestRpgApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestRpgApplication.class, args);
    }
}

package com.michael1099.rest_rpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:mail.yml")
public class RestRpgApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestRpgApplication.class, args);
    }

}

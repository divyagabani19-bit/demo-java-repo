package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Use this @EnableConfigurationProperties for autoLoad jwt properties

@SpringBootApplication
public class EcomProjectMavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcomProjectMavenApplication.class, args);
    }

}

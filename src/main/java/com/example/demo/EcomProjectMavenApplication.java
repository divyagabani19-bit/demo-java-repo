package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.demo.Security.Config.JwtConfig;

// Use this @EnableConfigurationProperties for autoLoad jwt properties
@EnableConfigurationProperties(JwtConfig.class)
@SpringBootApplication
public class EcomProjectMavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomProjectMavenApplication.class, args);
	}

}

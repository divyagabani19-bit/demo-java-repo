package com.example.demo.Security.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;

// Using @value
/*@Component
@Getter
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.issuer}")
    private String issuer;

 }*/

  @Component
  
  @ConfigurationProperties(prefix = "jwt")
  @Data
  public class JwtConfig {
  
  private String secret; private long expiration; private String issuer;
  
  }
 
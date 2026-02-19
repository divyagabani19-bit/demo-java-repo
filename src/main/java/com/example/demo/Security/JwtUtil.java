package com.example.demo.Security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.Security.Config.JwtConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jwtConfig;

    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(
                    new Date(System.currentTimeMillis() + jwtConfig.getExpiration())
                )
                .signWith(SignatureAlgorithm.HS256,
                          jwtConfig.getSecret())
                .compact();
    }

    public String extractUsername(String token) {

        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
  //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = new Date(System.currentTimeMillis() + jwtConfig.getExpiration());
        		return expiration.before(new Date());
    }

}


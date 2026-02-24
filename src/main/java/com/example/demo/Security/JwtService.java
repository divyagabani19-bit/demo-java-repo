package com.example.demo.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtsecret;

    @Value("${jwt.issuer}")
    private String jwtissuer;

    @Value("${jwt.refresh.expiration}")
    private long jwtrefreshexpiration;

    @Value("${jwt.access.expiration}")
    private long jwtaccessexpiration;

    // Generate Access Token
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "ACCESS");
        return buildToken(extraClaims, userDetails, jwtaccessexpiration);
    }

    // Generate Refresh Token
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "REFRESH");
        return buildToken(extraClaims, userDetails, jwtrefreshexpiration);
    }

    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {

        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuer(jwtissuer)
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, jwtsecret).compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder().setSigningKey(jwtsecret).build().parseClaimsJws(token).getBody();

    }

    public <T> T extractClaim(String token, Function<Claims, T> ClaimsResolver) {
        final Claims claims = extractAllClaims(token);
        return ClaimsResolver.apply(claims);
    }
}

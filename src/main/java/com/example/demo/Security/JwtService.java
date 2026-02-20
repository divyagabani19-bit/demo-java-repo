package com.example.demo.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.example.demo.Security.Config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;

@Component
public class JwtService {

	@Autowired
	private JwtConfig jwtConfig;

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<String, Object>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuer(jwtConfig.getIssuer()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
				.signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();
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

		return Jwts.parserBuilder().setSigningKey(jwtConfig.getSecret()).build().parseClaimsJws(token).getBody();

	}

	public <T> T extractClaim(String token, Function<Claims, T> ClaimsResolver) {
		final Claims claims = extractAllClaims(token);
		return ClaimsResolver.apply(claims);
	}
}

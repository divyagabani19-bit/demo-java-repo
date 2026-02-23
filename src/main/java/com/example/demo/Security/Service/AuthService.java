package com.example.demo.Security.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.Security.JwtRequest;
import com.example.demo.Security.JwtResponse;
import com.example.demo.Security.JwtService;
import com.example.demo.Security.Role;
import com.example.demo.Security.Model.AuthUser;
import com.example.demo.Security.Repo.AuthRepo;

@Service
public class AuthService {

    @Autowired
    private AuthRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse register(JwtRequest request) {
        var user = AuthUser.builder().name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        repo.save(user);
        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse login(JwtRequest request) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());
        try {
            authenticationManager.authenticate(authentication);
            var user = repo.findByName(request.getUsername()).orElseThrow();

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);
            repo.save(user);
            return new JwtResponse(accessToken, refreshToken);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    public JwtResponse refresh(Map<String, String> request) {

        var user = repo.findByName(request.get("username")).orElseThrow();
        String refreshToken = user.getRefreshToken();
        if (jwtService.isTokenValid(refreshToken, user)) {

            String newAccessToken = jwtService.generateAccessToken(user);
            return new JwtResponse(newAccessToken, refreshToken);

        }

        else
            return null;
    }

}

package com.example.demostore.controller;

import com.example.demostore.auth.AuthService;
import com.example.demostore.dtos.auth.JwtResponse;
import com.example.demostore.dtos.auth.LoginRequest;
import com.example.demostore.dtos.auth.RegisterRequest;
import com.example.demostore.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse authenticate(@RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<User.UserSummary> register(@RequestBody RegisterRequest registerRequest) {
        User registeredUser = authService.registerUser(registerRequest);
        return new ResponseEntity<>(User.UserSummary.fromUser(registeredUser), HttpStatus.CREATED);
    }
}

package com.susan.eventbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.susan.eventbooking.service.AuthService;
import com.susan.eventbooking.dto.AuthResponse;
import com.susan.eventbooking.dto.LoginRequest;
import com.susan.eventbooking.dto.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        logger.info("Received registration request for email: {}", request.getEmail());
        authService.register(request);
        logger.info("Registration response sent successfully for email: {}", request.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        logger.info("Received login request for email: {}", request.getEmail());
        
        String token = authService.login(request);
        
        logger.info("Login response sent successfully for email: {}", request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
}

package com.susan.eventbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.susan.eventbooking.dto.LoginRequest;
import com.susan.eventbooking.dto.RegisterRequest;
import com.susan.eventbooking.model.User;
import com.susan.eventbooking.repository.UserRepository;
import com.susan.eventbooking.security.JwtUtil;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        logger.info("Registering new user with email: {}", request.getEmail());
        
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // default role to USER when not provided
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        userRepository.save(user);
        
        logger.info("User registered successfully with email: {}", request.getEmail());
    }

    public String login(LoginRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());
        
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Login failed: User not found with email: {}", request.getEmail());
                    return new RuntimeException("User not found");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Invalid password for email: {}", request.getEmail());
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        logger.info("Login successful for email: {}", request.getEmail());
        return token;
    }
}

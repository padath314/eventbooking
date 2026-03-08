package com.susan.eventbooking.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.susan.eventbooking.dto.AuthResponse;
import com.susan.eventbooking.dto.LoginRequest;
import com.susan.eventbooking.dto.RegisterRequest;
import com.susan.eventbooking.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldRegisterUser() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        doNothing().when(authService).register(any(RegisterRequest.class));

        // When
        ResponseEntity<String> response = authController.register(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldLoginUser() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(authService.login(any(LoginRequest.class))).thenReturn("jwt.token.here");

        // When
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt.token.here", response.getBody().getToken());
        verify(authService, times(1)).login(any(LoginRequest.class));
    }
}
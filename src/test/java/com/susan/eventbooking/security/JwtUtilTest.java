package com.susan.eventbooking.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil();

    @Test
    void shouldGenerateToken() {
        // Given
        String email = "test@example.com";

        // When
        String token = jwtUtil.generateToken(email);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts separated by dots
    }

    @Test
    void shouldExtractEmailFromToken() {
        // Given
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        // When
        String extractedEmail = jwtUtil.extractEmail(token);

        // Then
        assertNotNull(extractedEmail);
        assertEquals(email, extractedEmail);
    }

    @Test
    void shouldValidateToken() {
        // Given
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        // When
        String extractedEmail = jwtUtil.extractEmail(token);

        // Then
        assertEquals(email, extractedEmail);
        assertNotNull(extractedEmail);
    }

    @Test
    void shouldHandleInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> jwtUtil.extractEmail(invalidToken));
    }

    @Test
    void shouldGenerateDifferentTokensForDifferentEmails() {
        // Given
        String email1 = "user1@example.com";
        String email2 = "user2@example.com";

        // When
        String token1 = jwtUtil.generateToken(email1);
        String token2 = jwtUtil.generateToken(email2);

        // Then
        assertNotEquals(token1, token2);
        assertEquals(email1, jwtUtil.extractEmail(token1));
        assertEquals(email2, jwtUtil.extractEmail(token2));
    }
}
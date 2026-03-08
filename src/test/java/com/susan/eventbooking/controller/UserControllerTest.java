package com.susan.eventbooking.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.susan.eventbooking.model.User;
import com.susan.eventbooking.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldCreateUser() {
        // Given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setRole("USER");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User response = userController.createUser(user);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldGetAllUsers() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> response = userController.getAllUsers();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        verify(userRepository).findAll();
    }

    @Test
    void shouldGetUserById() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User response = userController.getUserById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldUpdateUser() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old Name");

        User updatedUser = new User();
        updatedUser.setName("Updated Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // When
        User response = userController.updateUser(1L, updatedUser);

        // Then
        assertNotNull(response);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldDeleteUser() {
        // Given
        doNothing().when(userRepository).deleteById(1L);

        // When
        userController.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }
}
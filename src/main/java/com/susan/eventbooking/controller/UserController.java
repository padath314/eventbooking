package com.susan.eventbooking.controller;

import com.susan.eventbooking.model.User;
import com.susan.eventbooking.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        logger.info("Creating user with email: {}", user.getEmail());
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("Found {} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        logger.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        logger.info("User found with id: {}", id);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
        logger.info("User deleted successfully with id: {}", id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        logger.info("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        User savedUser = userRepository.save(existingUser);
        logger.info("User updated successfully with id: {}", id);
        return savedUser;
    }
}
package com.susan.eventbooking.repository;

import com.susan.eventbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom finder method
    Optional<User> findByEmail(String email);
}
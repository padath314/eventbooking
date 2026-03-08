package com.susan.eventbooking.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class UserTest {

    @Test
    void shouldCreateUser() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword");
        user.setRole("USER");

        // When & Then
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("hashedPassword", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    void shouldSetCreatedAtOnCreation() {
        // Given
        User user = new User();

        // When
        user.onCreate();

        // Then
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(user.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }
}

class EventTest {

    @Test
    void shouldCreateEvent() {
        // Given
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setLocation("Test Location");

        User creator = new User();
        creator.setId(1L);
        event.setCreatedBy(creator);

        // When & Then
        assertEquals(1L, event.getId());
        assertEquals("Test Event", event.getTitle());
        assertEquals("Test Description", event.getDescription());
        assertEquals("Test Location", event.getLocation());
        assertEquals(creator, event.getCreatedBy());
    }
}

class BookingTest {

    @Test
    void shouldCreateBooking() {
        // Given
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus("CONFIRMED");

        User user = new User();
        user.setId(1L);
        booking.setUser(user);

        Event event = new Event();
        event.setId(1L);
        booking.setEvent(event);

        // When & Then
        assertEquals(1L, booking.getId());
        assertEquals("CONFIRMED", booking.getStatus());
        assertEquals(user, booking.getUser());
        assertEquals(event, booking.getEvent());
    }

    @Test
    void shouldSetBookingTimeOnCreation() {
        // Given
        Booking booking = new Booking();

        // When
        booking.onCreate();

        // Then
        assertNotNull(booking.getBookingTime());
        assertTrue(booking.getBookingTime().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(booking.getBookingTime().isAfter(LocalDateTime.now().minusSeconds(1)));
    }
}
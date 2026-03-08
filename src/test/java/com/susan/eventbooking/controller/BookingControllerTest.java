package com.susan.eventbooking.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.susan.eventbooking.model.Booking;
import com.susan.eventbooking.model.Event;
import com.susan.eventbooking.model.User;
import com.susan.eventbooking.repository.BookingRepository;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void shouldCreateBooking() {
        // Given
        Booking booking = new Booking();
        User user = new User();
        user.setId(1L);
        Event event = new Event();
        event.setId(1L);

        booking.setUser(user);
        booking.setEvent(event);
        booking.setStatus("CONFIRMED");

        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setUser(user);
        savedBooking.setEvent(event);
        savedBooking.setStatus("CONFIRMED");

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        // When
        Booking response = bookingController.createBooking(booking);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("CONFIRMED", response.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void shouldGetAllBookings() {
        // Given
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStatus("CONFIRMED");

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStatus("PENDING");

        List<Booking> bookings = Arrays.asList(booking1, booking2);
        when(bookingRepository.findAll()).thenReturn(bookings);

        // When
        List<Booking> response = bookingController.getAllBookings();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteBooking() {
        // Given
        doNothing().when(bookingRepository).deleteById(1L);

        // When
        bookingController.deleteBooking(1L);

        // Then
        verify(bookingRepository, times(1)).deleteById(1L);
    }
}
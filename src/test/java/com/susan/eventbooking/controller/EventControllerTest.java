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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.susan.eventbooking.model.Event;
import com.susan.eventbooking.model.User;
import com.susan.eventbooking.service.EventService;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void shouldCreateEvent() {
        // Given
        Event event = new Event();
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setLocation("Test Location");

        User user = new User();
        user.setId(1L);
        event.setCreatedBy(user);

        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        // When
        ResponseEntity<Event> response = eventController.createEvent(event);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Event", response.getBody().getTitle());
        verify(eventService, times(1)).createEvent(any(Event.class));
    }

    @Test
    void shouldGetAllEvents() {
        // Given
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");

        List<Event> events = Arrays.asList(event1, event2);
        when(eventService.getAllEvents()).thenReturn(events);

        // When
        ResponseEntity<List<Event>> response = eventController.getAllEvents();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    void shouldGetEventById() {
        // Given
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");

        when(eventService.getEventById(1L)).thenReturn(event);

        // When
        ResponseEntity<Event> response = eventController.getEventById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Event", response.getBody().getTitle());
        verify(eventService, times(1)).getEventById(1L);
    }

    @Test
    void shouldUpdateEvent() {
        // Given
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Updated Event");

        when(eventService.updateEvent(eq(1L), any(Event.class))).thenReturn(event);

        // When
        ResponseEntity<Event> response = eventController.updateEvent(1L, event);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Event", response.getBody().getTitle());
        verify(eventService, times(1)).updateEvent(eq(1L), any(Event.class));
    }

    @Test
    void shouldDeleteEvent() {
        // Given
        doNothing().when(eventService).deleteEvent(1L);

        // When
        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).deleteEvent(1L);
    }
}

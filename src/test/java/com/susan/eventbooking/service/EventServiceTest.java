package com.susan.eventbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.susan.eventbooking.repository.EventRepository;
import com.susan.eventbooking.model.Event;
import com.susan.eventbooking.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void shouldCreateEvent() {
        Event event = new Event();
        event.setTitle("Music Concert");
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        Event savedEvent = eventService.createEvent(event);
        assertNotNull(savedEvent, "Saved event should not be null");
        assertEquals("Music Concert", savedEvent.getTitle(), "Event title should match");
        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
    }

    @Test
    void shouldReturnAllEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Concert");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Hackathon");

        List<Event> events = List.of(event1, event2);

        Mockito.when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getAllEvents();

        assertEquals(2, result.size());

        Mockito.verify(eventRepository).findAll();
    }

    @Test
    void shouldGetEventById() {
        // Given
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // When
        Event result = eventService.getEventById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Event", result.getTitle());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(1L));
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateEvent() {
        // Given
        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setTitle("Old Title");

        Event updatedEvent = new Event();
        updatedEvent.setTitle("New Title");
        updatedEvent.setDescription("New Description");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);

        // When
        Event result = eventService.updateEvent(1L, updatedEvent);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentEvent() {
        // Given
        Event updatedEvent = new Event();
        updatedEvent.setTitle("New Title");

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> eventService.updateEvent(1L, updatedEvent));
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void shouldDeleteEvent() {
        // Given
        Event event = new Event();
        event.setId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(event);

        // When
        eventService.deleteEvent(1L);

        // Then
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentEvent() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEvent(1L));
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, never()).deleteById(1L);
    }
}

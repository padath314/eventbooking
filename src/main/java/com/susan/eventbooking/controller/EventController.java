package com.susan.eventbooking.controller;

import com.susan.eventbooking.model.Event;
import com.susan.eventbooking.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    // ✅ Inject EventService
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // =========================
    // 🟢 CREATE Event
    // =========================
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        logger.info("Creating event with title: {}", event.getTitle());
        Event savedEvent = eventService.createEvent(event);
        logger.info("Event created successfully with id: {}", savedEvent.getId());
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED); // 201
    }

    // =========================
    // 🔵 GET All Events
    // =========================
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        logger.info("Fetching all events");
        List<Event> events = eventService.getAllEvents();
        logger.info("Found {} events", events.size());
        return ResponseEntity.ok(events); // 200
    }

    // =========================
    // 🟡 GET Event By ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        logger.info("Fetching event with id: {}", id);
        Event event = eventService.getEventById(id);
        logger.info("Event found with id: {}", id);
        return ResponseEntity.ok(event); // 200
    }

    // =========================
    // 🟠 UPDATE Event
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody Event event) {
        logger.info("Updating event with id: {}", id);
        Event updatedEvent = eventService.updateEvent(id, event);
        logger.info("Event updated successfully with id: {}", id);
        return ResponseEntity.ok(updatedEvent); // 200
    }

    // =========================
    // 🔴 DELETE Event
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Deleting event with id: {}", id);
        eventService.deleteEvent(id);
        logger.info("Event deleted successfully with id: {}", id);
        return ResponseEntity.noContent().build(); // 204
    }
}
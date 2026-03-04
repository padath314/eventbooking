package com.susan.eventbooking.controller;

import com.susan.eventbooking.model.Event;
import com.susan.eventbooking.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

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
        Event savedEvent = eventService.createEvent(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED); // 201
    }

    // =========================
    // 🔵 GET All Events
    // =========================
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents()); // 200
    }

    // =========================
    // 🟡 GET Event By ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id)); // 200
    }

    // =========================
    // 🟠 UPDATE Event
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody Event event) {

        return ResponseEntity.ok(eventService.updateEvent(id, event)); // 200
    }

    // =========================
    // 🔴 DELETE Event
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
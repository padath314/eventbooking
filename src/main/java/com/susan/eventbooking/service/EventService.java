package com.susan.eventbooking.service;

import com.susan.eventbooking.exception.ResourceNotFoundException;
import com.susan.eventbooking.model.Event;
import com.susan.eventbooking.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    // ✅ Inject EventRepository (Constructor Injection - BEST PRACTICE)
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // 1️⃣ Create Event
    public Event createEvent(Event event) {
        logger.info("Creating event with title: {}", event.getTitle());
        Event savedEvent = eventRepository.save(event);
        logger.info("Event created with id: {}", savedEvent.getId());
        return savedEvent;
    }

    // 2️⃣ Get All Events
    public List<Event> getAllEvents() {
        logger.info("Fetching all events");
        List<Event> events = eventRepository.findAll();
        logger.info("Found {} events", events.size());
        return events;
    }

    // 3️⃣ Get Event By ID
    public Event getEventById(Long id) {
        logger.info("Fetching event with id: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Event not found with id: {}", id);
                    return new ResourceNotFoundException("Event not found with id: " + id);
                });
        logger.info("Event retrieved with id: {}", id);
        return event;
    }

    // 4️⃣ Update Event
    public Event updateEvent(Long id, Event updatedEvent) {
        logger.info("Updating event with id: {}", id);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Event not found with id: {} for update", id);
                    return new ResourceNotFoundException("Event not found with id: " + id);
                });

        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setEventDate(updatedEvent.getEventDate());

        Event savedEvent = eventRepository.save(existingEvent);
        logger.info("Event updated successfully with id: {}", id);
        return savedEvent;
    }

    // 5️⃣ Delete Event
    public void deleteEvent(Long id) {
        logger.info("Deleting event with id: {}", id);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Event not found with id: {} for deletion", id);
                    return new ResourceNotFoundException("Event not found with id: " + id);
                });

        eventRepository.delete(existingEvent);
        logger.info("Event deleted successfully with id: {}", id);
    }
}
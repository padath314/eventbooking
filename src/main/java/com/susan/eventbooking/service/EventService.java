package com.susan.eventbooking.service;

import com.susan.eventbooking.exception.ResourceNotFoundException;
import com.susan.eventbooking.model.Event;
import com.susan.eventbooking.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    // ✅ Inject EventRepository (Constructor Injection - BEST PRACTICE)
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // 1️⃣ Create Event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // 2️⃣ Get All Events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 3️⃣ Get Event By ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Event not found with id: " + id));
    }

    // 4️⃣ Update Event
    public Event updateEvent(Long id, Event updatedEvent) {

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Event not found with id: " + id));

        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setEventDate(updatedEvent.getEventDate());

        return eventRepository.save(existingEvent);
    }

    // 5️⃣ Delete Event
    public void deleteEvent(Long id) {

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Event not found with id: " + id));

        eventRepository.delete(existingEvent);
    }
}
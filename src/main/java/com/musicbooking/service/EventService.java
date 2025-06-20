package com.musicbooking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musicbooking.dto.EventDTO;
import com.musicbooking.exception.ResourceNotFoundException;
import com.musicbooking.model.Event;
import com.musicbooking.model.User;
import com.musicbooking.repository.EventRepository;
import com.musicbooking.repository.UserRepository;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return convertToDto(event);
    }

    public List<EventDTO> getEventsByOrganizer(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EventDTO createEvent(Event event, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found with id: " + organizerId));
        
        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);
        return convertToDto(savedEvent);
    }

    public EventDTO updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        event.setName(eventDetails.getName());
        event.setVenue(eventDetails.getVenue());
        event.setAddress(eventDetails.getAddress());
        event.setStartDate(eventDetails.getStartDate());
        event.setEndDate(eventDetails.getEndDate());
        event.setDescription(eventDetails.getDescription());

        Event updatedEvent = eventRepository.save(event);
        return convertToDto(updatedEvent);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        eventRepository.delete(event);
    }

    public List<EventDTO> searchEvents(String query) {
        return eventRepository.searchEvents(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getUpcomingEvents() {
        return eventRepository.findEventsBetweenDates(LocalDateTime.now(), LocalDateTime.now().plusMonths(1))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Add this method to get Event entity by id
    public Event getEventByIdEntity(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    // Add this method to save Event entity
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    // Update convertToDto to include imageUrl
    public EventDTO convertToDto(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getVenue(),
                event.getAddress(),
                event.getStartDate(),
                event.getEndDate(),
                event.getDescription(),
                event.getOrganizer().getId(),
                event.getOrganizer().getFullName(),
                event.getImageUrl() // <-- add this line
        );
    }
}
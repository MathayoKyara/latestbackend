package com.musicbooking.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.musicbooking.dto.EventDTO;
import com.musicbooking.dto.TicketAnalyticsDTO;
import com.musicbooking.exception.ResourceNotFoundException;
import com.musicbooking.model.Event;
import com.musicbooking.model.TicketCategory;
import com.musicbooking.repository.EventRepository;
import com.musicbooking.repository.TicketCategoryRepository;
import com.musicbooking.service.EventService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/organizer/{organizerId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<EventDTO>> getEventsByOrganizer(@PathVariable Long organizerId) {
        return ResponseEntity.ok(eventService.getEventsByOrganizer(organizerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<EventDTO> createEvent(@RequestBody Event event, @RequestParam Long organizerId) {
        return ResponseEntity.ok(eventService.createEvent(event, organizerId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestParam String query) {
        return ResponseEntity.ok(eventService.searchEvents(query));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventDTO>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    @PostMapping("/{id}/upload-image")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<EventDTO> uploadEventImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String folder = "uploads/events/";
            Files.createDirectories(Paths.get(folder));
            String filename = id + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(folder + filename);
            file.transferTo(filePath);

            // Update event with image URL
            Event event = eventService.getEventByIdEntity(id);
            event.setImageUrl("/" + folder + filename);
            eventService.save(event);

            // Return the updated EventDTO
            return ResponseEntity.ok(eventService.convertToDto(event));
        } catch (IOException | IllegalStateException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{eventId}/analytics")
    public ResponseEntity<TicketAnalyticsDTO> getEventAnalytics(@PathVariable Long eventId) {
        List<TicketCategory> categories = ticketCategoryRepository.findByEventId(eventId);
        int sold = categories.stream().mapToInt(cat -> cat.getTotalQuantity() - cat.getRemainingQuantity()).sum();
        int received = sold; // or adjust if you track "received" differently
        int remaining = categories.stream().mapToInt(TicketCategory::getRemainingQuantity).sum();

        TicketAnalyticsDTO dto = new TicketAnalyticsDTO();
        dto.setSold(sold);
        dto.setReceived(received);
        dto.setRemaining(remaining);
        return ResponseEntity.ok(dto);
    }

    // Get booking status
    @GetMapping("/{eventId}/booking-status")
    public ResponseEntity<?> getBookingStatus(@PathVariable Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return ResponseEntity.ok(Map.of("bookingOpen", event.isBookingOpen()));
    }

    // Set booking status (admin only)
    @PutMapping("/{eventId}/booking-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setBookingStatus(@PathVariable Long eventId, @RequestParam boolean bookingOpen) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        event.setBookingOpen(bookingOpen);
        eventRepository.save(event);
        return ResponseEntity.ok(Map.of("bookingOpen", event.isBookingOpen()));
    }
}
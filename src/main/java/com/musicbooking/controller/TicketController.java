package com.musicbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musicbooking.dto.BookTicketRequest;
import com.musicbooking.dto.TicketDTO;
import com.musicbooking.exception.ResourceNotFoundException;
import com.musicbooking.model.Event;
import com.musicbooking.model.TicketCategory;
import com.musicbooking.repository.EventRepository;
import com.musicbooking.repository.TicketCategoryRepository;
import com.musicbooking.service.TicketService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getTicketsByEvent(eventId));
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(
            @RequestParam Long eventId,
            @RequestParam Long userId,
            @RequestParam String categoryClass) {
        return ResponseEntity.ok(ticketService.createTicket(eventId, userId, categoryClass));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate/{ticketHash}")
    public ResponseEntity<Boolean> validateTicket(@PathVariable String ticketHash) {
        return ResponseEntity.ok(ticketService.validateTicket(ticketHash));
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookTickets(@RequestBody BookTicketRequest req) {
        Event event = eventRepository.findById(req.getEventId())
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        if (!event.isBookingOpen()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Booking is closed for this event.");
        }
        TicketCategory cat = ticketCategoryRepository.findByEventIdAndCategoryClass(req.getEventId(), req.getCategoryClass())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (cat.getRemainingQuantity() < req.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough tickets available.");
        }
        // Book tickets (loop or batch)
        for (int i = 0; i < req.getQuantity(); i++) {
            // create and save Ticket entity as per your logic
            cat.setRemainingQuantity(cat.getRemainingQuantity() - 1);
        }
        ticketCategoryRepository.save(cat);
        return ResponseEntity.ok("Tickets booked successfully.");
    }
}
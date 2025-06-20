package com.musicbooking.service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musicbooking.dto.TicketDTO;
import com.musicbooking.exception.ResourceNotFoundException;
import com.musicbooking.model.Event;
import com.musicbooking.model.Ticket;
import com.musicbooking.model.TicketCategory;
import com.musicbooking.model.User;
import com.musicbooking.repository.EventRepository;
import com.musicbooking.repository.TicketRepository;
import com.musicbooking.repository.UserRepository;
import com.musicbooking.repository.TicketCategoryRepository;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                        EventRepository eventRepository,
                        UserRepository userRepository,
                        PaymentService paymentService) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
    }

    public TicketService(EventRepository eventRepository, PaymentService paymentService, TicketRepository ticketRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.paymentService = paymentService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return convertToDto(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketsByUser(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TicketDTO createTicket(Long eventId, Long userId, String categoryClass) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (categoryClass == null || categoryClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket category class cannot be empty");
        }

        // --- Ticket inventory logic ---
        TicketCategory ticketCategory = ticketCategoryRepository
            .findByEventIdAndCategoryClass(eventId, categoryClass)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (ticketCategory.getRemainingQuantity() <= 0) {
            throw new IllegalStateException("Tickets sold out for this category");
        }

        // Decrement and save
        ticketCategory.setRemainingQuantity(ticketCategory.getRemainingQuantity() - 1);
        ticketCategoryRepository.save(ticketCategory);
        // --- End inventory logic ---

        String ticketHash = generateUniqueTicketHash();
        Date expiryDate = Date.from(event.getEndDate().atZone(ZoneId.systemDefault()).toInstant());

        Ticket ticket = new Ticket();
        ticket.setTicketHash(ticketHash);
        ticket.setCategoryClass(categoryClass);
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setExpiryDate(expiryDate);
        ticket.setCreationDate(new Date());

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDto(savedTicket);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean validateTicket(String ticketHash) {
        Ticket ticket = ticketRepository.findByTicketHash(ticketHash)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with hash: " + ticketHash));
        return ticket.getExpiryDate().after(new Date());
    }

    private String generateUniqueTicketHash() {
        String ticketHash;
        do {
            ticketHash = UUID.randomUUID().toString();
        } while (ticketRepository.existsByTicketHash(ticketHash));
        return ticketHash;
    }

    private TicketDTO convertToDto(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO(
                ticket.getId(),
                ticket.getTicketHash(),
                ticket.getCategoryClass(),
                ticket.getEvent().getId(),
                ticket.getEvent().getName(),
                ticket.getUser().getId(),
                ticket.getUser().getFullName());

        if (ticket.getPayment() != null) {
            ticketDTO.setPayment(paymentService.convertToDto(ticket.getPayment()));
        }

        return ticketDTO;
    }

    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    public EventRepository getEventRepository() {
        return eventRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public TicketCategoryRepository getTicketCategoryRepository() {
        return ticketCategoryRepository;
    }

    public void setTicketCategoryRepository(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }
}

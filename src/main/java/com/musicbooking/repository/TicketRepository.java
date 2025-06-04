package com.musicbooking.repository;

import com.musicbooking.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByEventId(Long eventId);
    Optional<Ticket> findByTicketHash(String ticketHash);
    boolean existsByTicketHash(String ticketHash);
}

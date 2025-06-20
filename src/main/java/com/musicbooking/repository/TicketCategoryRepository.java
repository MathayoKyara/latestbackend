package com.musicbooking.repository;

import com.musicbooking.model.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    List<TicketCategory> findByEventId(Long eventId);
    Optional<TicketCategory> findByEventIdAndCategoryClass(Long eventId, String categoryClass);
}
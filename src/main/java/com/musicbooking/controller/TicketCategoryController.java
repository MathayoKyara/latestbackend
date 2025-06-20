package com.musicbooking.controller;

import com.musicbooking.dto.TicketCategoryDTO;
import com.musicbooking.service.TicketCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tickets/categories")
public class TicketCategoryController {
    @Autowired
    private TicketCategoryService ticketCategoryService;

    // Create categories (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTicketCategories(
            @RequestParam Long eventId,
            @RequestBody List<TicketCategoryDTO> categories) {
        ticketCategoryService.createOrUpdateCategories(eventId, categories);
        return ResponseEntity.ok().build();
    }

    // Update a single category (ADMIN only)
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketCategoryDTO> updateTicketCategory(
            @PathVariable Long categoryId,
            @RequestBody TicketCategoryDTO categoryDTO) {
        TicketCategoryDTO updated = ticketCategoryService.updateCategory(categoryId, categoryDTO);
        return ResponseEntity.ok(updated);
    }

    // Delete a category (ADMIN only)
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTicketCategory(@PathVariable Long categoryId) {
        ticketCategoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    // Get categories for an event (open to all)
    @GetMapping
    public ResponseEntity<List<TicketCategoryDTO>> getCategoriesByEvent(@RequestParam Long eventId) {
        return ResponseEntity.ok(ticketCategoryService.getCategoriesByEvent(eventId));
    }
}
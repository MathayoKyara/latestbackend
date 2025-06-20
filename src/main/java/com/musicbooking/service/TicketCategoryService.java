package com.musicbooking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musicbooking.dto.TicketCategoryDTO;
import com.musicbooking.exception.ResourceNotFoundException;
import com.musicbooking.model.Event;
import com.musicbooking.model.TicketCategory;
import com.musicbooking.repository.EventRepository;
import com.musicbooking.repository.TicketCategoryRepository;

@Service
public class TicketCategoryService {
    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;
    @Autowired
    private EventRepository eventRepository;

    public List<TicketCategoryDTO> getCategoriesByEvent(Long eventId) {
        return ticketCategoryRepository.findByEventId(eventId)
            .stream().map(this::toDto).collect(Collectors.toList());
    }

    public void createOrUpdateCategories(Long eventId, List<TicketCategoryDTO> dtos) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        for (TicketCategoryDTO dto : dtos) {
            TicketCategory cat = (dto.getId() != null)
                ? ticketCategoryRepository.findById(dto.getId()).orElse(new TicketCategory())
                : new TicketCategory();
            cat.setEvent(event);
            cat.setName(dto.getName());
            cat.setCategoryClass(dto.getCategoryClass());
            cat.setTotalQuantity(dto.getTotalQuantity());
            cat.setRemainingQuantity(dto.getRemainingQuantity());
            cat.setPrice(dto.getPrice());
            ticketCategoryRepository.save(cat);
        }
    }

    public void deleteCategory(Long id) {
        ticketCategoryRepository.deleteById(id);
    }

    private TicketCategoryDTO toDto(TicketCategory cat) {
        TicketCategoryDTO dto = new TicketCategoryDTO();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        dto.setCategoryClass(cat.getCategoryClass());
        dto.setTotalQuantity(cat.getTotalQuantity());
        dto.setRemainingQuantity(cat.getRemainingQuantity());
        dto.setPrice(cat.getPrice());
        return dto;
    }

    public TicketCategoryDTO updateCategory(Long categoryId, TicketCategoryDTO categoryDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
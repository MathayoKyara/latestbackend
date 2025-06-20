package com.musicbooking.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EventDTO {
    private Long id;
    private String name;
    private String venue;
    private String address;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    
    private String description;
    private Long organizerId;
    private String organizerName;
    private String imageUrl;

    // Constructors, Getters and Setters
    public EventDTO() {
    }

    public EventDTO(Long id, String name, String venue, String address, 
                   LocalDateTime startDate, LocalDateTime endDate, 
                   String description, Long organizerId, String organizerName,
                   String imageUrl) {
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
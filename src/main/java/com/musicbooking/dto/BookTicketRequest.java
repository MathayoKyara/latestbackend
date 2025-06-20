package com.musicbooking.dto;

import lombok.Data;

@Data
public class BookTicketRequest {
    private Long eventId;
    private Long userId;
    private String categoryClass;
    private int quantity;
}
package com.musicbooking.dto;

import lombok.Data;

@Data
public class TicketAnalyticsDTO {
    private int sold;
    private int received;
    private int remaining;
}
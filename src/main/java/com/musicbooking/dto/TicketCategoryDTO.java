package com.musicbooking.dto;

import lombok.Data;

@Data
public class TicketCategoryDTO {
    private Long id;
    private String name;
    private String categoryClass;
    private int totalQuantity;
    private int remainingQuantity;
    private int price;
}

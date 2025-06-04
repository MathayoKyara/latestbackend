package com.musicbooking.dto;

public class TicketDTO {
    private Long id;
    private String ticketHash;
    private String categoryClass;
    private Long eventId;
    private String eventName;
    private Long userId;
    private String userFullName;
    private PaymentDTO payment;

    // Constructors, Getters and Setters
    public TicketDTO() {
    }

    public TicketDTO(Long id, String ticketHash, String categoryClass, 
                    Long eventId, String eventName, Long userId, String userFullName) {
        this.id = id;
        this.ticketHash = ticketHash;
        this.categoryClass = categoryClass;
        this.eventId = eventId;
        this.eventName = eventName;
        this.userId = userId;
        this.userFullName = userFullName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketHash() {
        return ticketHash;
    }

    public void setTicketHash(String ticketHash) {
        this.ticketHash = ticketHash;
    }

    public String getCategoryClass() {
        return categoryClass;
    }

    public void setCategoryClass(String categoryClass) {
        this.categoryClass = categoryClass;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }
}
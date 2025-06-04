package com.musicbooking.dto;

import java.math.BigDecimal;

public class PaymentDTO {
    private Long id;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String paymentStatus;
    private String description;
    private Long ticketId;

    // Constructors, Getters and Setters
    public PaymentDTO() {
    }

    public PaymentDTO(Long id, BigDecimal amount, String paymentMethod, 
                     String transactionId, String paymentStatus, Long ticketId) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paymentStatus = paymentStatus;
        this.ticketId = ticketId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
}
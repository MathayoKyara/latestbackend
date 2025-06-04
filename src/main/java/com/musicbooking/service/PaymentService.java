
package com.musicbooking.service;

import com.musicbooking.dto.PaymentDTO;
import com.musicbooking.exception.ResourceNotFoundException;
import com.musicbooking.model.*;
import com.musicbooking.repository.PaymentRepository;
import com.musicbooking.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public PaymentDTO createPayment(Long ticketId, BigDecimal amount, String paymentMethod) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        String transactionId = generateTransactionId();

        Payment payment = new Payment(
                amount,
                paymentMethod,
                "COMPLETED",
                ticket);

        payment.setTransactionId(transactionId);
        payment.setDescription("Payment for ticket #" + ticket.getId() + " to event: " + ticket.getEvent().getName());

        Payment savedPayment = paymentRepository.save(payment);
        
        // Update ticket with payment
        ticket.setPayment(savedPayment);
        ticketRepository.save(ticket);

        return convertToDto(savedPayment);
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return convertToDto(payment);
    }

    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction id: " + transactionId));
        return convertToDto(payment);
    }

    public PaymentDTO updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        payment.setPaymentStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToDto(updatedPayment);
    }

    private String generateTransactionId() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public PaymentDTO convertToDto(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getPaymentStatus(),
                payment.getTicket().getId());
    }
}
package com.musicbooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicbooking.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTicket_Event_Id(Long eventId);

    Optional<Payment> findByTransactionId(String transactionId);
}
package com.musicbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.musicbooking.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
}
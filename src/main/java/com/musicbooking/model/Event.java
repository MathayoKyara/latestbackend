package com.musicbooking.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;  // Added import

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private String address;

    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "date_added")
    private Date dateAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets = new HashSet<>();

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean bookingOpen = true; // default to true

    // Constructors
    public Event() {
    }

    public Event(String name, String venue, String address, LocalDateTime startDate, 
                LocalDateTime endDate, String description, User organizer) {
        this.name = name;
        this.venue = venue;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.organizer = organizer;
    }

    // Getter and Setter
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBookingOpen() {
        return bookingOpen;
    }

    public void setBookingOpen(boolean bookingOpen) {
        this.bookingOpen = bookingOpen;
    }
}
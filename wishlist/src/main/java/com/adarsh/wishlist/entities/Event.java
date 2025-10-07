package com.adarsh.wishlist.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Minimal Event entity required to participate in the Many-to-Many
 * relationship with UserWishlist. In a real microservices architecture,
 * this entity might only store the ID and be used primarily for joining tables.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_item")
public class Event {

    // This ID is the unique identifier for the event (the Long from your original list)
    @Id
    private Long id;

    // Optional: Add basic details if you need them, e.g., name or service lookup key
    // private String eventName; 
}

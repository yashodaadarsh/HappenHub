package com.adarsh.wishlist.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_wishlist")
public class UserWishlist {

    @Id
    private String emailId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "wishlist_event_map", // Name of the joining table in the database
            joinColumns = @JoinColumn(name = "user_email_id"), // Column from this entity (UserWishlist)
            inverseJoinColumns = @JoinColumn(name = "event_id") // Column from the target entity (Event)
    )
    private List<Event> events;
}

package com.adarsh.wishlist.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_wishlist") // MongoDB collection
public class UserWishlist {

    @Id
    private String userId;

    // MongoDB supports arrays directly
    private List<Long> events; // use String if your event IDs are UUIDs/strings
}

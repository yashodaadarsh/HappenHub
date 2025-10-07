package com.adarsh.wishlist.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NotificationModel {
    private String email;
    List<EventModel> events;
}

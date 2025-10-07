package com.adarsh.wishlist.service;

import com.adarsh.wishlist.model.EventModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationSender {

    public void sendExpiryNotification(String userEmail, List<EventModel> events) {
        StringBuilder message = new StringBuilder();
        message.append("Dear user, the following events in your wishlist will expire tomorrow:\n\n");

        for (EventModel e : events) {
            message.append("- ").append(e.getTitle())
                    .append(" (Ends on: ").append(e.getEndDate()).append(")\n");
        }

        System.out.println("📧 Sending notification to " + userEmail + ":\n" + message);
        // In real implementation → use JavaMailSender, Twilio, or Firebase Cloud Messaging
    }
}

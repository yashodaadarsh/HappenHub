package com.adarsh.MailService.service;

import com.adarsh.wishlist.model.EventModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendExpiryNotification(String toEmail, List<EventModel> events) {
        if (events == null || events.isEmpty()) return;

        String subject = "Your Wishlist Events Expire Tomorrow";
        String body = buildEmailBody(events);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    private String buildEmailBody(List<EventModel> events) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear User,\n\n");
        sb.append("The following events in your wishlist will expire tomorrow:\n\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

        for (EventModel e : events) {
            sb.append("Title: ").append(e.getTitle()).append("\n")
              .append("Location: ").append(e.getLocation()).append("\n")
              .append("End Date: ").append(
                      e.getEndDate().toInstant().atZone(ZoneId.of("Asia/Kolkata")).format(formatter)
              ).append("\n")
              .append("Link: ").append(e.getEventLink()).append("\n\n");
        }

        sb.append("Please take necessary action before the events expire.\n\n");
        sb.append("Regards,\nHappenHub Team");
        return sb.toString();
    }
}

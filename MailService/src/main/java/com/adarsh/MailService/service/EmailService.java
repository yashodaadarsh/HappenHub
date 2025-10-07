package com.adarsh.MailService.service;

import com.adarsh.MailService.model.EventModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    public void sendExpiryNotification(String toEmail, List<EventModel> events) {
        if (events == null || events.isEmpty()) return;

        String subject = "Your Wishlist Events Expire Tomorrow";
        String body = buildHtmlEmailBody(events);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // true => send HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildHtmlEmailBody(List<EventModel> events) {

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>")
                .append("<html><body style='font-family:Arial,sans-serif;'>")
                .append("<h2 style='color:#2E86C1;'>Dear User,</h2>")
                .append("<p>The following events in your wishlist will expire tomorrow:</p>")
                .append("<table style='border-collapse:collapse;width:100%;'>")
                .append("<tr>")
                .append("<th style='border:1px solid #ddd;padding:8px;background-color:#f2f2f2;'>Title</th>")
                .append("<th style='border:1px solid #ddd;padding:8px;background-color:#f2f2f2;'>Location</th>")
                .append("<th style='border:1px solid #ddd;padding:8px;background-color:#f2f2f2;'>End Date</th>")
                .append("<th style='border:1px solid #ddd;padding:8px;background-color:#f2f2f2;'>Type</th>") // Added Type column
                .append("<th style='border:1px solid #ddd;padding:8px;background-color:#f2f2f2;'>Link</th>")
                .append("</tr>");

        for (EventModel e : events) {
            sb.append("<tr>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(e.getTitle()).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(e.getLocation()).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>")
                    .append(e.getEndDate().toInstant().toString())
                    .append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(e.getType()).append("</td>") // Event type
                    .append("<td style='border:1px solid #ddd;padding:8px;'><a href='")
                    .append(e.getEventLink()).append("' target='_blank'>View Event</a></td>")
                    .append("</tr>");
        }

        sb.append("</table>")
                .append("<p>Please take necessary action before the events expire.</p>")
                .append("<p>Regards,<br><b>HappenHub Team</b></p>")
                .append("</body></html>");

        return sb.toString();
    }

}

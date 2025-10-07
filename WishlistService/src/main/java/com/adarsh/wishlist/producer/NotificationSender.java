package com.adarsh.wishlist.producer;

import com.adarsh.wishlist.model.EventModel;
import com.adarsh.wishlist.model.NotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationSender {

    private final KafkaTemplate<String, NotificationModel> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;
    @Autowired
    public NotificationSender(KafkaTemplate<String, NotificationModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendExpiryNotification(String userEmail, List<EventModel> events) {

        NotificationModel notificationModel = NotificationModel.builder()
                                                    .email(userEmail)
                                                    .events(events)
                                                    .build();


        Message<NotificationModel> message = MessageBuilder.withPayload(notificationModel)
                .setHeader(KafkaHeaders.TOPIC,TOPIC_NAME).build();
        kafkaTemplate.send(message);

    }
}

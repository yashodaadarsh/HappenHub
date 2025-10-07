package com.adarsh.eventProducer;

import com.adarsh.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private final KafkaTemplate<String, EventModel> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;
    @Autowired
    public EventProducer(KafkaTemplate<String, EventModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(EventModel eventModel){
        Message<EventModel> message = MessageBuilder.withPayload(eventModel)
                .setHeader(KafkaHeaders.TOPIC,TOPIC_NAME).build();
        kafkaTemplate.send(message);
    }

}

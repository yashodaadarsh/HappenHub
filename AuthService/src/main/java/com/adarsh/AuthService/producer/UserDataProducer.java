package com.adarsh.AuthService.producer;

import com.adarsh.AuthService.Model.UserDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserDataProducer {
    private final KafkaTemplate<String, UserDataModel> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    public UserDataProducer(KafkaTemplate<String, UserDataModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserDataModel userDataModel){
        Message<UserDataModel> message = MessageBuilder.withPayload(userDataModel)
                .setHeader(KafkaHeaders.TOPIC,TOPIC_NAME).build();
        kafkaTemplate.send(message);
    }
}

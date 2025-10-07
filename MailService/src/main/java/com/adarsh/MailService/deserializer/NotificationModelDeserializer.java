package com.adarsh.MailService.deserializer;

import com.adarsh.MailService.model.NotificationModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class NotificationModelDeserializer implements Deserializer<NotificationModel> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NotificationModel deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                throw new SerializationException("Null data received for topic: " + topic);
            }
            NotificationModel dto = objectMapper.readValue(data, NotificationModel.class);
            return dto;
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message from topic: " + topic, e);
        }
    }
}

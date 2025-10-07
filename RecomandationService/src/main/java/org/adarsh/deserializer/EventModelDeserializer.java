package org.adarsh.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.adarsh.model.EventModel;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class EventModelDeserializer implements Deserializer<EventModel> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public EventModel deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                throw new SerializationException("Null data received for topic: " + topic);
            }
            EventModel dto = objectMapper.readValue(data, EventModel.class);
            return dto;
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message from topic: " + topic, e);
        }
    }
}

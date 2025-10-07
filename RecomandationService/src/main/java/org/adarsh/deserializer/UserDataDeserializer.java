package org.adarsh.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.adarsh.model.UserDataModel;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;


public class UserDataDeserializer implements Deserializer<UserDataModel> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserDataModel deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                throw new SerializationException("Null data received for topic: " + topic);
            }
            UserDataModel dto = objectMapper.readValue(data, UserDataModel.class);
            return dto;
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message from topic: " + topic, e);
        }
    }
}

package com.adarsh.AuthService.serializer;

import com.adarsh.AuthService.Model.UserDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class UserDataSerializer implements Serializer<UserDataModel> {
    @Override
    public byte[] serialize(String topic, UserDataModel data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return retVal;
    }
}

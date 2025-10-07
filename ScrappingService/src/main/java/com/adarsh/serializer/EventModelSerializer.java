package com.adarsh.serializer;

import com.adarsh.model.EventModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;


public class EventModelSerializer implements Serializer<EventModel> {

    @Override
    public byte[] serialize(String s, EventModel eventModel ) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            retVal = objectMapper.writeValueAsString(eventModel).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return retVal;
    }
}

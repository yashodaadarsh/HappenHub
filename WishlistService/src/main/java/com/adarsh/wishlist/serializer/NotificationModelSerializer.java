package com.adarsh.wishlist.serializer;

import com.adarsh.wishlist.model.NotificationModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class NotificationModelSerializer implements Serializer<NotificationModel> {

    @Override
    public byte[] serialize(String s, NotificationModel notificationModel) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            retVal = objectMapper.writeValueAsString(notificationModel).getBytes();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return retVal;
    }
}

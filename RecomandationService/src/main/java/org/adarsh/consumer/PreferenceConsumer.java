package org.adarsh.consumer;

import org.adarsh.model.EventModel;
import org.adarsh.model.UserDataModel;
import org.adarsh.service.EventService;
import org.adarsh.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PreferenceConsumer {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserDataService userDataService;

    @KafkaListener(
        topics = "event_data",
        groupId = "event-consumer-groupppmmnb",
        containerFactory = "eventKafkaListenerFactory"
    )
    public void consumeEvent(EventModel eventModel) {
        // process and save to preferencedb
        eventService.createOrUpdateUser(eventModel);
    }

    @KafkaListener(
        topics = "user_data",
        groupId = "user-consumer-group",
        containerFactory = "userKafkaListenerFactory"
    )
    public void consumeUser(UserDataModel userDataModel) {
        // process and save to preferencedb
        userDataService.createOrUpdate(userDataModel);
    }
}

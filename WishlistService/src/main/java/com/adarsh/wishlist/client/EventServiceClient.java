package com.adarsh.wishlist.client;


import com.adarsh.wishlist.model.EventModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "scrapping", url = "http://localhost:8082/event-service/api/v1/events") // replace with your service URL
public interface EventServiceClient {

    @GetMapping("/{id}")
    EventModel getEventById(@PathVariable("id") Long id);

    @GetMapping
    List<EventModel> getAllEvents();

    @GetMapping("/by-ids")
    List<EventModel> getEventsByIds(@RequestParam("eventsIdList") List<Long> eventsIdList);

}

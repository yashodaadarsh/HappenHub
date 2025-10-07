package com.adarsh.wishlist.client;

import org.adarsh.model.EventModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "scrapping", url = "http://localhost:8080/") // replace with your service URL
public interface EventServiceClient {

    @GetMapping("/event/{id}")
    EventModel getEventById(@PathVariable("id") Long id);

    @GetMapping("/event/getall")
    List<EventModel> getAllEvents();

    @PostMapping("/event/createUpdate")
    EventModel createOrUpdateEvent(@RequestBody EventModel eventModel);

    @GetMapping("/event/get/{type}")
    List<EventModel> getEventsByType(@PathVariable("type") String type);

    @GetMapping("/event/getpage")
    List<EventModel> getEventsWithPagination(@RequestParam int page, @RequestParam int size);

    @GetMapping("/event/getByIds")
    List<EventModel> getEventsByIds(List<Long> eventsIdList);

    @GetMapping("/api/v1/scrap/internshala/jobs")
    List<EventModel> scrapInternshalaJobs();

    @GetMapping("/api/v1/scrap/internshala/internships")
    List<EventModel> scrapInternshalaInternships();

    @GetMapping("/api/v1/scrap/devpost/hackathons")
    List<EventModel> scrapDevPostHackathons();}

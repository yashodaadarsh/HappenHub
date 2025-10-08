package org.adarsh.controller;

import org.adarsh.model.EventModel;
import org.adarsh.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recommendation-service/api/v1/events")
public class RecommendationController { 

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventModel> createOrUpdateEvent(@RequestBody EventModel eventModel ){
        try{
            EventModel event = eventService.createOrUpdateUser(eventModel);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping 
    public ResponseEntity<List<EventModel>> getAllEvents(){
        List<EventModel> events = eventService.findAll();
        return ResponseEntity.ok(events); 
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getPersonalizedFeed(
            @RequestHeader("X-email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try{
            List<EventModel> feed = eventService.getFeed(email, page, size);
            return ResponseEntity.ok(feed);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email id is not present. Please Register or provide a valid 'X-email' header.");
        }
    }
}
package org.adarsh.controller;

import org.adarsh.model.EventModel;
import org.adarsh.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class RecomandationController {

    @Autowired
    private EventService eventService;

    @PostMapping("/createupdate")
    public ResponseEntity<EventModel> createUpdate(@RequestBody EventModel eventModel ){
        try{
            EventModel event = eventService.createOrUpdateUser(eventModel);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<EventModel>> getAllEvent(){
        List<EventModel> events = eventService.findAll();
        return new ResponseEntity<>(events,HttpStatus.OK);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<EventModel>> getFeed(
            @RequestHeader("X-email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<EventModel> feed = eventService.getFeed(email, page, size);
        return ResponseEntity.ok(feed);

    }


}

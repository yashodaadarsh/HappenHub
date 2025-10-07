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
public class EventController {

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

    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEvent(@PathVariable Long id){
        try{
            EventModel event = eventService.getEvent(id);
            return new ResponseEntity<>(event,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<EventModel>> getAllEvent(){
        List<EventModel> events = eventService.findAll();
        return new ResponseEntity<>(events,HttpStatus.OK);
    }

    @GetMapping("/getpage")
    public ResponseEntity<List<EventModel>> getEventsWithPagination(@RequestParam int page , @RequestParam int size ){
        List<EventModel> events = eventService.getEventsWithPagination(page,size);
        return new ResponseEntity<>(events,HttpStatus.OK);
    }

    @GetMapping("/get/{type}")
    public ResponseEntity<List<EventModel>> findByType(@PathVariable String type ){
        List<EventModel> events = eventService.findByType(type);
        return new ResponseEntity<>(events,HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchQuery( @PathVariable String query ){
        List<EventModel> events = eventService.searchQuery( query );
        return new ResponseEntity<>(events,HttpStatus.OK);
    }

}

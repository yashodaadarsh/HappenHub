package org.adarsh.controller;

import org.adarsh.model.EventModel;
import org.adarsh.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("search-service/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventModel> createOrUpdateEvent(@RequestBody EventModel eventModel ){
        try{
            EventModel event = eventService.createOrUpdateEvent(eventModel);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable Long id){
        try{
            EventModel event = eventService.getEvent(id);
            return ResponseEntity.ok(event); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventModel>> getAllEvents(){
        List<EventModel> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/paged")
    public ResponseEntity<List<EventModel>> getEventsWithPagination(
            @RequestParam(defaultValue = "0") int page , 
            @RequestParam(defaultValue = "10") int size ){
        
        List<EventModel> events = eventService.getEventsWithPagination(page,size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EventModel>> getEventsByType(@PathVariable String type ){
        List<EventModel> events = eventService.findByType(type);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchQuery(@RequestParam String q ){
        List<EventModel> events = eventService.searchQuery( q );
        return ResponseEntity.ok(events);
    }
}
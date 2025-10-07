package org.adarsh.controller;

import org.adarsh.model.EventModel;
import org.adarsh.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("event-service/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventModel> createEvent(@RequestBody EventModel eventModel) {
        try {
            EventModel event = eventService.createOrUpdateUser(eventModel);
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventModel> updateEvent(@PathVariable Long id, @RequestBody EventModel eventModel) {
        try {
            EventModel event = eventService.createOrUpdateUser(eventModel);
            return new ResponseEntity<>(event, HttpStatus.OK); // 200 OK for successful updates
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable Long id) {
        try {
            EventModel event = eventService.getEvent(id);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<EventModel>> getAllEvents() {
        List<EventModel> events = eventService.findAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<EventModel>> getEventsByIds(@RequestParam("eventsIdList") List<Long> ids) {
        List<EventModel> events = eventService.findByIds(ids);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
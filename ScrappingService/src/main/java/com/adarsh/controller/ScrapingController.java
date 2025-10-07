package com.adarsh.controller;

import com.adarsh.eventProducer.EventProducer;
import com.adarsh.model.EventModel;
import com.adarsh.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data")
public class ScrapingController {

    @Autowired
    private ScrapingService scrapingService;

    @Autowired
    private EventProducer eventProducer;


    @GetMapping("/internshala/jobs")
    public ResponseEntity<?> getInternshalaJobs() {
        List<EventModel> events = null;
        try {
            events = scrapingService.scrapIntenshalaJobs();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to retrieve Internshala job data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/internshala/internships")
    public ResponseEntity<?> getInternshalaInternships() {
        List<EventModel> events = null;
        try {
            events = scrapingService.scrapIntenshalaInternships();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to retrieve Internshala internship data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/devposts/hackathons")
    public ResponseEntity<?> getDevPostHackathons() {
        List<EventModel> events = null;
        try {
            events = scrapingService.scrapDevPostHackthons();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to retrieve Devpost hackathon data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/events")
    public ResponseEntity<String> createEvent(@RequestBody EventModel eventModel) {
        try {
            eventProducer.sendEventToKafka(eventModel);
            return new ResponseEntity<>("Event published successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to publish event.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
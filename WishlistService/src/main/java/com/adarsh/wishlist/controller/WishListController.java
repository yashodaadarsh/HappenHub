package com.adarsh.wishlist.controller;

import com.adarsh.wishlist.entities.UserWishlist;
import com.adarsh.wishlist.model.EventModel;
import com.adarsh.wishlist.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("wishlist-service/api/v1/wishlist")
public class WishListController {

    @Autowired
    private WishListService service;

    @PostMapping("/events/{eventId}")
    public ResponseEntity<UserWishlist> addEventToWishlist(
            @PathVariable("eventId") Long eventId,
            @RequestHeader("X-email") String userEmail) {

        UserWishlist userWishlist = service.addEvent(eventId, userEmail);

        return new ResponseEntity<>(userWishlist, HttpStatus.OK);
    }

    @PatchMapping("/events/{id}")
    public ResponseEntity<UserWishlist> removeEvent(@PathVariable Long id, @RequestHeader("X-email") String userEmail) {
        UserWishlist userWishlist = service.removeEvent(id, userEmail);
        return new ResponseEntity<>(userWishlist, HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventModel>> getWishlistEvents(@RequestHeader("X-email") String userEmail) {
        try {
            List<EventModel> events = service.getAllEvents(userEmail);
            return ResponseEntity.ok(events);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
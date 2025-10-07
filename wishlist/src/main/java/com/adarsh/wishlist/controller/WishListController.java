package com.adarsh.wishlist.controller;

import org.adarsh.entities.UserWishlist;
import org.adarsh.model.EventModel;
import org.adarsh.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    private WishListService service;

    @PostMapping("/add/{id}")
    public ResponseEntity<UserWishlist> addEvent(@PathVariable Long id ){
        UserWishlist userWishlist = service.addEvent(id);
        return new ResponseEntity<>(userWishlist, HttpStatus.CREATED);
    }

    @PatchMapping("/remove/{id}")
    public ResponseEntity<UserWishlist> removeEvent(@PathVariable Long id ){
        UserWishlist userWishlist = service.removeEvent(id);
        return new ResponseEntity<>(userWishlist,HttpStatus.ACCEPTED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<EventModel>> getAll(){
        List<EventModel> events = service.getAllEvents();
        return ResponseEntity.ok(events);
    }

}

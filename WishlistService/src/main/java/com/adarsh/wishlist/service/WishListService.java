package com.adarsh.wishlist.service;


import com.adarsh.wishlist.client.EventServiceClient;
import com.adarsh.wishlist.entities.Event;
import com.adarsh.wishlist.entities.UserWishlist;
import com.adarsh.wishlist.model.EventModel;
import com.adarsh.wishlist.repository.EventRepository;
import com.adarsh.wishlist.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListService {

    @Autowired
    private EventServiceClient eventServiceClient;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private EventRepository eventRepository;


    public UserWishlist addEvent(Long id, String userEmail) {

        Event eventToAdd = eventRepository.findById(id)
                .orElseGet(() -> {
                    Event newEvent = new Event();
                    newEvent.setId(id);
                    return eventRepository.save(newEvent);
                });

        UserWishlist userWishlist = wishListRepository.findById(userEmail)
                .orElseGet(() ->
                        UserWishlist.builder()
                                .emailId(userEmail)
                                .events(new ArrayList<>())
                                .build()
                );

        if (!userWishlist.getEvents().contains(eventToAdd)) {
            userWishlist.getEvents().add(eventToAdd);
        } else {
            System.out.println("Warning: Event ID " + id + " is already in the wishlist.");
        }

        return wishListRepository.save(userWishlist);
    }

    public UserWishlist removeEvent(Long eventId, String userEmail){
        UserWishlist userWishlist = wishListRepository.findById(userEmail).get();

        List<Event> events = userWishlist.getEvents();

        boolean removed = events.removeIf(event -> event.getId().equals(eventId));

        if (!removed) {
            System.out.println("Warning: Event ID " + eventId + " was not found in the wishlist to be removed.");
        }

        return wishListRepository.save(userWishlist);
    }

    public List<EventModel> getAllEvents(String userEmail){
        UserWishlist userWishlist = wishListRepository.findById(userEmail).get();
        List<Event> events = userWishlist.getEvents();
        List<Long> eventsIdList = events.stream().map(Event::getId).collect(Collectors.toList());
        System.out.println(eventsIdList);
        return eventServiceClient.getEventsByIds(eventsIdList);
    }

}

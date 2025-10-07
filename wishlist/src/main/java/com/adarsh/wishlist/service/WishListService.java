package com.adarsh.wishlist.service;

import org.adarsh.client.EventServiceClient;
import org.adarsh.entities.UserInfo;
import org.adarsh.entities.UserWishlist;
import org.adarsh.model.EventModel;
import org.adarsh.repository.UserRepository;
import org.adarsh.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventServiceClient eventServiceClient;

    public UserWishlist addEvent(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        UserInfo userInfo = userRepository.findByEmail(email);
        UserWishlist userWishlist = wishListRepository.findByUserId(userInfo.getUserId()).orElse(
                UserWishlist.builder()
                        .userId(userInfo.getUserId())
                        .events(new ArrayList<>())
                        .build()
        );
        userWishlist.getEvents().add(id);
        wishListRepository.save(userWishlist);
        return userWishlist;
    }

    public UserWishlist removeEvent(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        UserInfo userInfo = userRepository.findByEmail(email);
        UserWishlist userWishlist = wishListRepository.findByUserId(userInfo.getUserId()).get();
        List<Long> eventsIdList = userWishlist.getEvents();
        if(eventsIdList.remove(id)){
            userWishlist.setEvents(eventsIdList);
            wishListRepository.save(userWishlist);
        }
        return userWishlist;
    }

    public List<EventModel> getAllEvents(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        UserInfo userInfo = userRepository.findByEmail(email);
        UserWishlist userWishlist = wishListRepository.findByUserId(userInfo.getUserId()).get();
        List<Long> eventsIdList = userWishlist.getEvents();
        return eventServiceClient.getEventsByIds(eventsIdList);
    }


}

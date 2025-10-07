package com.adarsh.wishlist.service;

import com.adarsh.wishlist.client.EventServiceClient;
import com.adarsh.wishlist.entities.UserWishlist;
import com.adarsh.wishlist.model.EventModel;
import com.adarsh.wishlist.producer.NotificationSender;
import com.adarsh.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final WishListRepository wishListRepository;
    private final EventServiceClient eventServiceClient;
    private final NotificationSender notificationSender;

    @Scheduled(cron = "0 0 0 * * *")
    public void notifyExpiringEvents() {
        List<UserWishlist> allWishlists = wishListRepository.findAll();

        for (UserWishlist wishlist : allWishlists) {
            List<Long> eventIds = wishlist.getEvents()
                    .stream()
                    .map(e -> e.getId())
                    .collect(Collectors.toList());

            if (eventIds.isEmpty()) continue;

            List<EventModel> events = eventServiceClient.getEventsByIds(eventIds);

            List<EventModel> expiringTomorrow = events.stream()
                    .filter(this::isExpiringTomorrow)
                    .collect(Collectors.toList());

            if (!expiringTomorrow.isEmpty()) {
                notificationSender.sendExpiryNotification(wishlist.getEmailId(), expiringTomorrow);
            }
        }
    }

    private boolean isExpiringTomorrow(EventModel event) {
        if (event.getEndDate() == null) return false;

        LocalDate eventEndDate = event.getEndDate().toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDate();

        LocalDate tomorrow = LocalDate.now(ZoneId.of("Asia/Kolkata")).plusDays(1);


        return eventEndDate.equals(tomorrow);

    }

}

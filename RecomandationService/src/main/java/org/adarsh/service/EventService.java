package org.adarsh.service;


import lombok.RequiredArgsConstructor;
import org.adarsh.entities.Event;
import org.adarsh.entities.UserData;
import org.adarsh.model.EventModel;
import org.adarsh.repository.EventRepository;
import org.adarsh.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService
{
    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final UserDataRepository userDataRepository;

    public EventModel createOrUpdateUser(EventModel eventModel){

        UnaryOperator<Event> updatingEvent = event -> {
            updateEvent(event,eventModel);
            return eventRepository.save(event);
        };

        Supplier<Event> createEvent = () -> {
            return eventRepository.save(eventModel.transformToEvent());
        };

        Event eventInfo = eventRepository.findById(eventModel.getEventId())
                .map(updatingEvent)
                .orElseGet(createEvent);

        return EventModel.builder()
                .eventId(eventInfo.getEventId())
                .title(eventInfo.getTitle())
                .imageUrl(eventInfo.getImageUrl())
                .eventLink(eventInfo.getEventLink())
                .location(eventInfo.getLocation())
                .salary(eventInfo.getSalary())
                .type(eventInfo.getType())
                .startDate(eventInfo.getStartDate())
                .endDate(eventInfo.getEndDate())
                .description(eventInfo.getDescription())
                .build();
    }

    private void updateEvent(Event event, EventModel eventModel) {
        event.setEventId(eventModel.getEventId());
        event.setTitle(eventModel.getTitle());
        event.setImageUrl(eventModel.getImageUrl());
        event.setEventLink(eventModel.getEventLink());
        event.setLocation(eventModel.getLocation());
        event.setSalary(eventModel.getSalary());
        event.setStartDate(eventModel.getStartDate());
        event.setEndDate(eventModel.getEndDate());
        event.setType(eventModel.getType());
        event.setDescription(eventModel.getDescription());
    }



    public List<EventModel> findAll() {
        List<Event> events = eventRepository.findAll();
        List<EventModel> eventModels = new ArrayList<>();
        for( Event event : events ){
            eventModels.add(convertToEventModel(event));
        }
        return eventModels;
    }

    private EventModel convertToEventModel(Event event) {
        return EventModel.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .imageUrl(event.getImageUrl())
                .eventLink(event.getEventLink())
                .location(event.getLocation())
                .salary(event.getSalary())
                .type(event.getType())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .type(event.getType())
                .description(event.getDescription())
                .build();
    }

    public List<EventModel> getFeed(String email, int page, int size) {
        UserData userData = userDataRepository.findById(email).orElseThrow();
        List<String> preferences = userData.getPreferences()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = eventRepository.findByTypeIn(preferences, pageable);

        return eventsPage.getContent()
                .stream()
                .map(this::convertToEventModel)
                .collect(Collectors.toList());
    }

}
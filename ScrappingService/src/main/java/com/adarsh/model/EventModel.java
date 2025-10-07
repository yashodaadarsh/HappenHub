package com.adarsh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventModel {
    private Long eventId;
    private String title;
    private String imageUrl;
    private String eventLink;
    private String location;
    private String salary;
    private String startDate;
    private String endDate;
    private String type;
    private String description;
}

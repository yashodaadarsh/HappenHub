package org.adarsh.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.adarsh.entities.Event;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventModel {
    private Long eventId;
    private String title;
    private String imageUrl;
    private String eventLink;
    private String location;
    private String salary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private String type;
    private String description;

    public Event transformToEvent() {
        return Event.builder()
                .eventId(this.getEventId())
                .title(this.getTitle())
                .imageUrl(this.getImageUrl())
                .eventLink(this.getEventLink())
                .location(this.getLocation())
                .salary(this.getSalary())
                .type(this.getType())
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .type(this.type)
                .description(this.getDescription())
                .build();
    }
}

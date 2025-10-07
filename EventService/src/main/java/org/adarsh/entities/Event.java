package org.adarsh.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    @Id
    private Long eventId;
    private String title;
    private String imageUrl;
    private String eventLink;
    private String location;
    private String salary;
    private Date startDate;
    private Date endDate;
    private String type;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

}

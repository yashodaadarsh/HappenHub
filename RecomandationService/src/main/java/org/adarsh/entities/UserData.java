package org.adarsh.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.adarsh.enums.PreferenceType;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "user")
public class UserData {

    @Id
    private String email;

    @ElementCollection(targetClass = PreferenceType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_preferences",            // custom table name
            joinColumns = @JoinColumn(name = "email") // foreign key column
    )
    @Column(name = "preferences")              // column storing enum values
    private List<PreferenceType> preferences;
}

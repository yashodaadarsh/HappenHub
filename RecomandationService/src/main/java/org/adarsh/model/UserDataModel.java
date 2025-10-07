package org.adarsh.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.adarsh.enums.PreferenceType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataModel {
    private String email;
    private List<PreferenceType> preferences;
}

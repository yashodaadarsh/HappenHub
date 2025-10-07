package com.adarsh.AuthService.Model;

import com.adarsh.AuthService.enums.PreferenceType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDataModel {
    private String email;
    private List<PreferenceType> preferences;
}

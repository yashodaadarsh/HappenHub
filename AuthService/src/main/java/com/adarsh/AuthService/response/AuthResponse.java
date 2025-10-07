package com.adarsh.AuthService.response;

import com.adarsh.AuthService.enums.PreferenceType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {
    private String email;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String address;
    private List<PreferenceType> preferences;
    private String token;
}

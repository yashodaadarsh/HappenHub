package com.adarsh.AuthService.request;

import com.adarsh.AuthService.enums.PreferenceType;
import lombok.Data;

import java.util.List;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String address;
    private List<PreferenceType> preferences;
}

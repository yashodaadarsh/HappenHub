package com.adarsh.AuthService.service;

import com.adarsh.AuthService.Model.UserDataModel;
import com.adarsh.AuthService.entity.User;
import com.adarsh.AuthService.producer.UserDataProducer;
import com.adarsh.AuthService.repository.UserDetailsRepository;
import com.adarsh.AuthService.request.AuthRequest;
import com.adarsh.AuthService.response.AuthResponse;
import com.adarsh.AuthService.response.UserDetailsDTO;
import com.adarsh.AuthService.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDataProducer userDataProducer;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDetailsRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }

    public AuthResponse generateToken(AuthRequest authRequest){
        User user = userDetailsRepository.findByEmail(authRequest.getEmail()).get();
        return AuthResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .preferences(user.getPreferences())
                .token(jwtUtil.generateToken(user.getEmail()))
                .build();
    }

    public boolean isSignUp( AuthRequest authRequest ){
        Optional<User> userOpt = userDetailsRepository.findByEmail(authRequest.getEmail());
        return userOpt.isPresent();
    }

    public AuthResponse register(AuthRequest authRequest) {
        User user = convertToUser(authRequest);
        User savedUser = userDetailsRepository.save(user);

        UserDataModel userDataModel = convertToUserDataModel(savedUser);
        userDataProducer.sendEventToKafka(userDataModel);

        return AuthResponse.builder()
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .phoneNumber(savedUser.getPhoneNumber())
                .address(savedUser.getAddress())
                .preferences(savedUser.getPreferences())
                .token(jwtUtil.generateToken(savedUser.getEmail()))
                .build();
    }

    private UserDataModel convertToUserDataModel(User savedUser) {
        return UserDataModel.builder()
                .email(savedUser.getEmail())
                .preferences(savedUser.getPreferences())
                .build();
    }

    private User convertToUser(AuthRequest authRequest) {
        return User.builder()
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role("ROLE_USER")
                .firstName(authRequest.getFirstName())
                .lastName(authRequest.getLastName())
                .phoneNumber(authRequest.getPhoneNumber())
                .address(authRequest.getAddress())
                .preferences(authRequest.getPreferences())
                .build();
    }

    public UserDetailsDTO getDetails(String email) {
        User user = userDetailsRepository.findByEmail(email).get();
        return UserDetailsDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .preferences(user.getPreferences())
                .build();
    }
}

package com.adarsh.AuthService.controller;

import com.adarsh.AuthService.request.AuthRequest;
import com.adarsh.AuthService.response.UserDetailsDTO;
import com.adarsh.AuthService.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("auth-service/api/v1")
public class AuthController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest){
        if( customUserDetailsService.isSignUp(authRequest) ){
            return new ResponseEntity<>("User with this email already exists." , HttpStatus.CONFLICT);
        }
        else {
            return new ResponseEntity<>(customUserDetailsService.register(authRequest) , HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),authRequest.getPassword()
                    )
            );
            return ResponseEntity.ok(customUserDetailsService.generateToken( authRequest ));
        } catch (Exception e) {
            return new ResponseEntity<>("Bad Credentials. Please try with correct email and password",HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String username = authentication.getName();
        return ResponseEntity.ok().body(customUserDetailsService.getDetails(username));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserDetailsDTO> putDetails(@RequestBody AuthRequest authRequest ) {
        UserDetailsDTO updatedUser = customUserDetailsService.update( authRequest );
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String username = authentication.getName();

        Map<String, String> responseBody = Collections.singletonMap("email", username);

        return ResponseEntity.ok().body(responseBody);
    }
    
}

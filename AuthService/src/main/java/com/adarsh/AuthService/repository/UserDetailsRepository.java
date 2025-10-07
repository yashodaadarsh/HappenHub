package com.adarsh.AuthService.repository;

import com.adarsh.AuthService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
}

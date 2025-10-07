package com.adarsh.wishlist.repository;

import com.adarsh.wishlist.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}

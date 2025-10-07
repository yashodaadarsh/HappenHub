package org.adarsh.repository;

import org.adarsh.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    Page<Event> findByTypeIn(List<String> types, Pageable pageable);
}

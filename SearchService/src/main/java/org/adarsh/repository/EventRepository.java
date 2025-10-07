package org.adarsh.repository;

import org.adarsh.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {

    Optional<Event> findByEventId(Long eventId);

    List<Event> findByType(String type);

    @Query(value = "SELECT * FROM event e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.type) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.location) LIKE LOWER(CONCAT('%', :searchQuery, '%'))", nativeQuery = true)
    List<Event> findByTitleOrDescriptionOrType(@Param("searchQuery") String searchQuery);

}

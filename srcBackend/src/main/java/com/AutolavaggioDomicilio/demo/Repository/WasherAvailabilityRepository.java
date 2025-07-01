package com.AutolavaggioDomicilio.demo.Repository;

import com.AutolavaggioDomicilio.demo.Entity.Washer;
import com.AutolavaggioDomicilio.demo.Entity.WasherAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WasherAvailabilityRepository extends JpaRepository<WasherAvailability, Long> {
    // Existing methods...

    @Query("SELECT CASE WHEN COUNT(wa) > 0 THEN true ELSE false END " +
            "FROM WasherAvailability wa " +
            "WHERE wa.washer.id = :washerId " +
            "AND wa.availableFrom < :end " +
            "AND wa.availableTo > :start")

    boolean existsByWasherAndTimeRange(
            @Param("washerId") Long washerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    Optional<WasherAvailability> findByWasherAndAvailableFromBetween(
            Washer washer,
            LocalDateTime start,
            LocalDateTime end);
}

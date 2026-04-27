package de.bdr.asset.management.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * JPA Booking Repository
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"user", "asset"})
    Optional<Booking> findById(Long id);

    @EntityGraph(attributePaths = {"user", "asset"})
    Page<Booking> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE asset_booking_mgm.booking SET status = 'CANCELLED' " +
                   "WHERE user_id = :userId " +
                   "AND status IN :targetStatuses",
           nativeQuery = true)
    void cancelNotFinishedBookingsForUser(
            @Param("userId") Long userId,
            @Param("targetStatuses") List<String> targetStatuses
    );
}

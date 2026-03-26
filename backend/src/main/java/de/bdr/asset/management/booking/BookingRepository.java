package de.bdr.asset.management.booking;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Booking Repository
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

}

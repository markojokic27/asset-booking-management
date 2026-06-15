package de.bdr.asset.management.booking;

import de.bdr.asset.management.core.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("bookingAuth")
@RequiredArgsConstructor
public class BookingAuthorizationEvaluator {

    private final BookingRepository bookingRepository;

    public boolean canManageBooking(Authentication authentication, Long bookingId) {

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> ("ROLE_ADMIN").equals(a.getAuthority()))) {
            return true;
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        return booking.getUser().getManagerEmail().equalsIgnoreCase(currentUser.getEmail());
    }

    public boolean canCreateBooking(Authentication authentication, Long targetUserId) {

        if (targetUserId == null) return true;

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> ("ROLE_ADMIN").equals(a.getAuthority()))) {
            return true;
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        // self-booking with explicit id
        return user.getId().equals(targetUserId);
    }

    public boolean canUpdateBooking(Authentication authentication, Long bookingId) {

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> ("ROLE_ADMIN").equals(a.getAuthority()))) {
            return true;
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        return currentUser.getId().equals(booking.getUser().getId());
    }
}

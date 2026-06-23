package de.bdr.asset.management.booking;

import de.bdr.asset.management.user.UserSoftDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingUserEventListener {

    private final BookingRepository bookingRepository;

    @EventListener
    @Transactional
    public void handleUserSoftDeleted(UserSoftDeletedEvent event) {
        List<String> statusesToCancel = List.of(
                BookingStatusEnum.APPROVED.name(),
                BookingStatusEnum.PENDING.name()
        );
        bookingRepository.cancelNotFinishedBookingsForUser(
                event.userId(), statusesToCancel);
    }
}

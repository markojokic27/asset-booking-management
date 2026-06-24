package de.bdr.asset.management.booking;

import de.bdr.asset.management.user.UserSoftDeletedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingUserEventListenerTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingUserEventListener listener;

    @Test
    void shouldCancelActiveBookingsOnUserSoftDelete() {

        UserSoftDeletedEvent event = new UserSoftDeletedEvent(this, 42L);

        listener.handleUserSoftDeleted(event);

        verify(bookingRepository).cancelNotFinishedBookingsForUser(
                eq(42L),
                anyList()
        );
    }
}

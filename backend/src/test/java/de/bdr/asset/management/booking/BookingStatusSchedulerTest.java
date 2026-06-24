package de.bdr.asset.management.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingStatusSchedulerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingStatusScheduler scheduler;

    @Test
    void shouldDelegateToService() {

        when(bookingService.bookingStatusToCompleted()).thenReturn(5);

        scheduler.closingBooking();

        verify(bookingService).bookingStatusToCompleted();
    }
}

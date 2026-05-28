package com.example.assetbookingmanagement.features.booking.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookingRepositoryTest {

    @Mock
    lateinit var bookingApi: BookingApi

    private lateinit var repository: BookingRepository

    @Before
    fun setUp() {
        repository = BookingRepository(bookingApi)
    }

    @Test
    fun testGetUserBookings() = runTest {
        val userId = 2L
        val expectedBookings = listOf(buildBookingResponse(id = 1L), buildBookingResponse(id = 2L))

        `when`(bookingApi.getBookings(userId = userId)).thenReturn(
            BookingListResponse(content = expectedBookings)
        )

        val result = repository.getUserBookings(userId)

        verify(bookingApi).getBookings(userId = userId)
        assertEquals(expectedBookings, result)
    }

    @Test
    fun testCreateBooking() = runTest {
        val request = BookingCreateRequest(
            userId = 2L,
            assetId = 1L,
            bookingStart = "2026-01-04T09:00:00Z",
            bookingEnd = "2026-01-14T09:00:00Z",
            notes = "Some optional notes"
        )
        val expectedBooking = buildBookingResponse(id = 3L)

        `when`(bookingApi.createBooking(request)).thenReturn(expectedBooking)

        val result = repository.createBooking(request)

        verify(bookingApi).createBooking(request)
        assertEquals(expectedBooking, result)
    }

    private fun buildBookingResponse(id: Long) = BookingResponse(
        id = id,
        user = UserSummary(
            id = 2L,
            name = "Ivan",
            surname = "Horvat",
            email = "ivan@example.com",
            role = "ADMIN"
        ),
        asset = AssetSummary(
            id = 1L,
            name = "Parking A-01",
            category = CategorySummary(
                id = 3L,
                name = "Parking",
                bookingPeriod = "DAY",
                approval = true
            ),
            status = "ACTIVE",
            description = "Outdoor parking",
            location = "Garage A"
        ),
        status = "PENDING",
        bookingStart = "2026-01-04T09:00:00Z",
        bookingEnd = "2026-01-14T09:00:00Z",
        notes = "Some optional notes"
    )
}

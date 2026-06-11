package com.example.assetbookingmanagement.features.home.ui

import com.example.assetbookingmanagement.features.asset.data.AssetApi
import com.example.assetbookingmanagement.features.asset.data.AssetListResponse
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import com.example.assetbookingmanagement.features.asset.ui.MainDispatcherRule
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.AssetSummary
import com.example.assetbookingmanagement.features.booking.data.BookingApi
import com.example.assetbookingmanagement.features.booking.data.BookingCreateRequest
import com.example.assetbookingmanagement.features.booking.data.BookingListResponse
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import com.example.assetbookingmanagement.features.booking.data.BookingResponse
import com.example.assetbookingmanagement.features.booking.data.CategorySummary
import com.example.assetbookingmanagement.features.booking.data.UserSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testInitLoadsAssetAndBookingCounts() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeAssetApi = FakeAssetApi().apply {
            response = AssetListResponse(
                content = listOf(
                    buildAssetResponse(id = 1L, name = "Hp 15"),
                    buildAssetResponse(id = 2L, name = "Projector Epson"),
                    buildAssetResponse(id = 3L, name = "Parking Spot 10")
                )
            )
        }
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(id = 1L),
                    buildBookingResponse(id = 2L)
                )
            )
        }
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )
        advanceUntilIdle()

        assertEquals(3, viewModel.uiState.value.assetCount)
        assertEquals(2, viewModel.uiState.value.myBookingsCount)
        assertEquals(1, fakeAssetApi.getAssetsCalls)
        assertEquals(1, fakeBookingApi.getBookingsCalls)
    }

    @Test
    fun testInitShowsZeroBookingsWhenUserIsMissing() = runTest {
        val fakeBookingApi = FakeBookingApi()
        val authSession = mock(AuthSession::class.java)

        `when`(authSession.getCurrentUserId()).thenReturn(null)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(FakeAssetApi()),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )
        advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.myBookingsCount)
        assertEquals(0, fakeBookingApi.getBookingsCalls)
    }

    @Test
    fun testInitShowsZeroAssetsWhenAssetRequestFails() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetsException = RuntimeException("Asset request failed")
        }
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(buildBookingResponse(id = 1L))
            )
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )
        advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.assetCount)
        assertEquals(1, viewModel.uiState.value.myBookingsCount)
    }

    @Test
    fun testInitShowsZeroBookingsWhenBookingRequestFails() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeAssetApi = FakeAssetApi().apply {
            response = AssetListResponse(
                content = listOf(buildAssetResponse(id = 1L, name = "Hp 15"))
            )
        }
        val fakeBookingApi = FakeBookingApi().apply {
            getBookingsException = RuntimeException("Booking request failed")
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.assetCount)
        assertEquals(0, viewModel.uiState.value.myBookingsCount)
    }

    private fun buildAssetResponse(
        id: Long,
        name: String
    ) = AssetResponse(
        id = id,
        name = name,
        categoryId = 1L,
        description = "Laptop located in room 301",
        code = "QR-LAPTOP-001",
        status = "ACTIVE",
        location = "Room 301"
    )

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

    private class FakeAssetApi : AssetApi {
        var response: AssetListResponse = AssetListResponse(content = emptyList())
        var getAssetsCalls: Int = 0
        var getAssetsException: Exception? = null

        override suspend fun getAssets(page: Int, size: Int): AssetListResponse {
            getAssetsCalls++
            getAssetsException?.let { throw it }
            return response
        }

        override suspend fun getAssetById(id: Long): AssetResponse {
            error("getAssetById is not used in HomeViewModel tests.")
        }
    }

    private class FakeBookingApi : BookingApi {
        var response: BookingListResponse = BookingListResponse(content = emptyList())
        var getBookingsCalls: Int = 0
        var getBookingsException: Exception? = null

        override suspend fun getBookings(
            userId: Long?,
            assetId: Long?,
            page: Int,
            size: Int
        ): BookingListResponse {
            getBookingsCalls++
            getBookingsException?.let { throw it }
            return response
        }

        override suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
            error("createBooking is not used in HomeViewModel tests.")
        }
    }
}

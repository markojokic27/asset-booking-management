package com.example.assetbookingmanagement.features.home.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assetbookingmanagement.core.ui.theme.AssetBookingManagementTheme
import com.example.assetbookingmanagement.features.asset.data.AssetApi
import com.example.assetbookingmanagement.features.asset.data.AssetListResponse
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.AssetSummary
import com.example.assetbookingmanagement.features.booking.data.BookingApi
import com.example.assetbookingmanagement.features.booking.data.BookingCreateRequest
import com.example.assetbookingmanagement.features.booking.data.BookingListResponse
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import com.example.assetbookingmanagement.features.booking.data.BookingResponse
import com.example.assetbookingmanagement.features.booking.data.BookingStatusUpdateRequest
import com.example.assetbookingmanagement.features.booking.data.CategorySummary
import com.example.assetbookingmanagement.features.booking.data.RecurringBookingCreateRequest
import com.example.assetbookingmanagement.features.booking.data.UserSummary
import com.example.assetbookingmanagement.features.user.data.ChangePasswordRequest
import com.example.assetbookingmanagement.features.user.data.UserApi
import com.example.assetbookingmanagement.features.user.data.UserRepository
import com.example.assetbookingmanagement.features.user.data.UserResponse
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testShowsCountsAndCallsCallbacks() {
        val authSession = AuthSession().apply {
            saveTokens(
                accessToken = "header.eyJ1c2VySWQiOiIyIn0.signature",
                refreshToken = "refresh-token"
            )
        }
        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(
                FakeAssetApi(
                    AssetListResponse(
                        content = listOf(
                            buildAssetResponse(id = 1L, name = "Hp 15"),
                            buildAssetResponse(id = 2L, name = "Projector Epson")
                        )
                    )
                )
            ),
            bookingRepository = BookingRepository(
                FakeBookingApi(
                    BookingListResponse(
                        content = listOf(buildBookingResponse(id = 1L))
                    )
                )
            ),
            authSession = authSession,
            userRepository = UserRepository(FakeUserApi())
        )
        var assetsClicks = 0
        var bookingsClicks = 0

        composeRule.setContent {
            AssetBookingManagementTheme {
                HomeScreen(
                    onAssetsClick = { assetsClicks++ },
                    onBookingsClick = { bookingsClicks++ },
                    viewModel = viewModel
                )
            }
        }

        composeRule.onNodeWithText("All assets").assertIsDisplayed()
        composeRule.onNodeWithText("My bookings").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("1").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Open All assets").performClick()
        composeRule.onNodeWithContentDescription("Open My bookings").performClick()

        assertEquals(1, assetsClicks)
        assertEquals(1, bookingsClicks)
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

    private class FakeAssetApi(
        private val response: AssetListResponse
    ) : AssetApi {
        override suspend fun getAssets(page: Int, size: Int): AssetListResponse = response

        override suspend fun getAssetById(id: Long): AssetResponse {
            error("getAssetById is not used in HomeScreen tests.")
        }
    }

    private class FakeBookingApi(
        private val response: BookingListResponse
    ) : BookingApi {
        override suspend fun getBookings(
            userId: Long?,
            assetId: Long?,
            status: String?,
            page: Int,
            size: Int
        ): BookingListResponse = response

        override suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
            error("createBooking is not used in HomeScreen tests.")
        }

        override suspend fun createRecurringBooking(request: RecurringBookingCreateRequest): List<BookingResponse> {
            error("createRecurringBooking is not used in HomeScreen tests.")
        }

        override suspend fun approveBooking(bookingId: Long): BookingResponse {
            error("approveBooking is not used in HomeScreen tests.")
        }

        override suspend fun rejectBooking(bookingId: Long): BookingResponse {
            error("rejectBooking is not used in HomeScreen tests.")
        }

        override suspend fun updateBooking(
            bookingId: Long,
            request: BookingStatusUpdateRequest
        ): BookingResponse {
            error("updateBooking is not used in HomeScreen tests.")
        }
    }

    private class FakeUserApi : UserApi {
        override suspend fun getUserById(id: Long): UserResponse = UserResponse(
            id = id,
            username = "ivan.horvat",
            surname = "Horvat",
            name = "Ivan",
            email = "ivan@example.com",
            role = "USER",
            status = "ACTIVE",
            departmentId = 1L,
            managerEmail = "manager@example.com",
            notes = null,
            benefit = null
        )

        override suspend fun changePassword(id: Long, request: ChangePasswordRequest) {
            error("changePassword is not used in HomeScreen tests.")
        }
    }
}

package com.example.assetbookingmanagement.features.booking.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.asset.ui.MainDispatcherRule
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class ApprovalRequestsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testLoadsMatchingRequestsForManager() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(
                        id = 10L,
                        assetName = "Hp 15",
                        categoryName = "Laptops",
                        bookingPeriod = "DAY",
                        managerEmail = "manager@example.com"
                    ),
                    buildBookingResponse(
                        id = 11L,
                        assetName = "Parking Spot 10",
                        categoryName = "Parking",
                        bookingPeriod = "DAY",
                        managerEmail = "ivan@example.com"
                    ),
                    buildBookingResponse(
                        id = 12L,
                        assetName = "Dell Latitude 14",
                        categoryName = "Laptops",
                        bookingPeriod = "DAY",
                        managerEmail = " manager@example.com "
                    )
                )
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(
                role = "MANAGER",
                email = " Manager@Example.com "
            )
        }

        val viewModel = ApprovalRequestsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(2, viewModel.uiState.value.requests.size)
        assertEquals("Hp 15", viewModel.uiState.value.requests.first().assetName)
        assertFalse(viewModel.uiState.value.requests.first().isHourlyBooking)
        assertEquals("Ivan Horvat", viewModel.uiState.value.requests.first().requesterName)
        assertEquals(null, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testLoadsAllRequestsForAdmin() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(id = 20L, managerEmail = "manager@example.com"),
                    buildBookingResponse(id = 21L, assetName = "Hp 15", categoryName = "Laptops", bookingPeriod = "DAY", managerEmail = "ivan@example.com"),
                    buildBookingResponse(id = 22L, assetName = "Parking Spot 10", categoryName = "Parking", bookingPeriod = "DAY", managerEmail = null)
                )
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(
                role = "ADMIN",
                email = "admin@example.com"
            )
        }

        val viewModel = ApprovalRequestsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(3, viewModel.uiState.value.requests.size)
        assertEquals(null, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testShowsNoAccessForRegularUser() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(buildBookingResponse(id = 1L))
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(role = "USER")
        }

        val viewModel = ApprovalRequestsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.requests.isEmpty())
        assertEquals(R.string.approvals_error_no_access, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testShowsErrorWhenUserMissing() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(null)

        val viewModel = ApprovalRequestsViewModel(
            bookingRepository = BookingRepository(FakeBookingApi()),
            authSession = authSession,
            userRepository = UserRepository(FakeUserApi())
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.requests.isEmpty())
        assertEquals(R.string.common_error_missing_logged_in_user, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testSearchFiltersRequests() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(
                        id = 10L,
                        assetName = "Hp 15",
                        categoryName = "Laptops",
                        bookingPeriod = "DAY",
                        managerEmail = "manager@example.com"
                    ),
                    buildBookingResponse(
                        id = 11L,
                        assetName = "Dell Latitude 14",
                        categoryName = "Laptops",
                        bookingPeriod = "DAY",
                        managerEmail = "manager@example.com"
                    )
                )
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(
                role = "MANAGER",
                email = "manager@example.com"
            )
        }

        val viewModel = ApprovalRequestsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        advanceUntilIdle()

        viewModel.onSearchTextChange("hp")

        assertEquals(1, viewModel.uiState.value.filteredRequests.size)
        assertEquals("Hp 15", viewModel.uiState.value.filteredRequests.first().assetName)
    }

    @Test
    fun testShowsErrorWhenLoadingFails() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            getBookingsException = RuntimeException("Booking request failed")
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(
                role = "ADMIN",
                email = "admin@example.com"
            )
        }

        val viewModel = ApprovalRequestsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(R.string.approvals_error_load_message, viewModel.uiState.value.errorMessageResId)
    }

    private fun buildBookingResponse(
        id: Long,
        assetName: String = "Hp 15",
        categoryName: String = "Laptops",
        bookingPeriod: String = "DAY",
        managerEmail: String? = "manager@example.com"
    ) = BookingResponse(
        id = id,
        user = UserSummary(
            id = 2L,
            name = "Ivan",
            surname = "Horvat",
            email = "ivan@example.com",
            role = "USER",
            managerEmail = managerEmail
        ),
        asset = AssetSummary(
            id = 1L,
            name = assetName,
            category = CategorySummary(
                id = 1L,
                name = categoryName,
                bookingPeriod = bookingPeriod,
                approval = true
            ),
            status = "ACTIVE",
            description = "Laptop located in room 301",
            location = "Room 301"
        ),
        status = "PENDING",
        bookingStart = "2026-01-04T09:00:00Z",
        bookingEnd = "2026-01-14T09:00:00Z",
        notes = null
    )

    private fun buildUserResponse(
        role: String,
        email: String = "ivan@example.com"
    ) = UserResponse(
        id = 2L,
        username = "ivan.horvat",
        surname = "Horvat",
        name = "Ivan",
        email = email,
        role = role,
        status = "ACTIVE",
        departmentId = 1L,
        managerEmail = "manager@example.com",
        notes = null,
        benefit = null
    )

    private class FakeBookingApi : BookingApi {
        var response: BookingListResponse = BookingListResponse(content = emptyList())
        var getBookingsException: Exception? = null

        override suspend fun getBookings(
            userId: Long?,
            assetId: Long?,
            status: String?,
            page: Int,
            size: Int
        ): BookingListResponse {
            getBookingsException?.let { throw it }

            val filteredContent = response.content.filter { booking ->
                val matchesUser = userId == null || booking.user.id == userId
                val matchesAsset = assetId == null || booking.asset.id == assetId
                val matchesStatus = status == null || booking.status.equals(status, ignoreCase = true)
                matchesUser && matchesAsset && matchesStatus
            }

            return response.copy(content = filteredContent.take(size))
        }

        override suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
            error("createBooking is not used in ApprovalRequestsViewModel tests.")
        }

        override suspend fun createRecurringBooking(request: RecurringBookingCreateRequest): List<BookingResponse> {
            error("createRecurringBooking is not used in ApprovalRequestsViewModel tests.")
        }

        override suspend fun approveBooking(bookingId: Long): BookingResponse {
            error("approveBooking is not used in ApprovalRequestsViewModel tests.")
        }

        override suspend fun rejectBooking(bookingId: Long): BookingResponse {
            error("rejectBooking is not used in ApprovalRequestsViewModel tests.")
        }

        override suspend fun updateBooking(
            bookingId: Long,
            request: BookingStatusUpdateRequest
        ): BookingResponse {
            error("updateBooking is not used in ApprovalRequestsViewModel tests.")
        }
    }

    private class FakeUserApi : UserApi {
        var response: UserResponse = UserResponse(
            id = 2L,
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
        var getUserException: Exception? = null

        override suspend fun getUserById(id: Long): UserResponse {
            getUserException?.let { throw it }
            return response
        }

        override suspend fun changePassword(id: Long, request: ChangePasswordRequest) {
            error("changePassword is not used in ApprovalRequestsViewModel tests.")
        }
    }
}

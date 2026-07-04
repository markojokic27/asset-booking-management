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
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(role = "USER")
        }
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(3, viewModel.uiState.value.assetCount)
        assertEquals(2, viewModel.uiState.value.myBookingsCount)
        assertEquals(false, viewModel.uiState.value.canManageApprovals)
        assertEquals(0, viewModel.uiState.value.pendingApprovalsCount)
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
            authSession = authSession,
            userRepository = UserRepository(FakeUserApi())
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.myBookingsCount)
        assertEquals(false, viewModel.uiState.value.canManageApprovals)
        assertEquals(0, viewModel.uiState.value.pendingApprovalsCount)
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
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(role = "USER")
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
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
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(role = "USER")
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.assetCount)
        assertEquals(0, viewModel.uiState.value.myBookingsCount)
    }

    @Test
    fun testInitLoadsOnlyMatchingPendingApprovalsForManager() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(id = 10L, managerEmail = "manager@example.com"),
                    buildBookingResponse(id = 11L, managerEmail = "ivan@example.com"),
                    buildBookingResponse(id = 12L, managerEmail = "manager@example.com")
                )
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(
                role = "MANAGER",
                email = "manager@example.com"
            )
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(FakeAssetApi()),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.canManageApprovals)
        assertEquals(2, viewModel.uiState.value.pendingApprovalsCount)
        assertEquals(2, fakeBookingApi.getBookingsCalls)
    }

    @Test
    fun testMatchesManagerPendingApprovalsIgnoringEmailCaseAndSpaces() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(id = 10L, managerEmail = "  MANAGER@EXAMPLE.COM  "),
                    buildBookingResponse(id = 11L, managerEmail = "ivan@example.com"),
                    buildBookingResponse(id = 12L, managerEmail = " manager@example.com ")
                )
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(
                role = "MANAGER",
                email = " Manager@Example.com "
            )
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(FakeAssetApi()),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.canManageApprovals)
        assertEquals(2, viewModel.uiState.value.pendingApprovalsCount)
    }

    @Test
    fun testInitDoesNotLoadPendingApprovalsForRegularUser() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(buildBookingResponse(id = 1L))
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(role = "USER")
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(FakeAssetApi()),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.canManageApprovals)
        assertEquals(0, viewModel.uiState.value.pendingApprovalsCount)
        assertEquals(1, fakeBookingApi.getBookingsCalls)
    }

    @Test
    fun testInitLoadsAllPendingApprovalsForAdmin() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(id = 20L, managerEmail = "manager@example.com"),
                    buildBookingResponse(id = 21L, managerEmail = "ivan@example.com"),
                    buildBookingResponse(id = 22L, managerEmail = null)
                )
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            response = buildUserResponse(
                role = "ADMIN",
                email = "admin@example.com"
            )
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(FakeAssetApi()),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.canManageApprovals)
        assertEquals(3, viewModel.uiState.value.pendingApprovalsCount)
        assertEquals(2, fakeBookingApi.getBookingsCalls)
    }

    @Test
    fun testResetsApprovalStateWhenUserRoleRequestFails() = runTest {
        val authSession = mock(AuthSession::class.java)
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(buildBookingResponse(id = 1L))
            )
        }
        val fakeUserApi = FakeUserApi().apply {
            getUserException = RuntimeException("User request failed")
        }

        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val viewModel = HomeViewModel(
            assetRepository = AssetRepository(FakeAssetApi()),
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession,
            userRepository = UserRepository(fakeUserApi)
        )
        viewModel.refreshHomeData()
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.canManageApprovals)
        assertEquals(0, viewModel.uiState.value.pendingApprovalsCount)
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

    private fun buildBookingResponse(
        id: Long,
        managerEmail: String? = "manager@example.com"
    ) = BookingResponse(
        id = id,
        user = UserSummary(
            id = 2L,
            name = "Ivan",
            surname = "Horvat",
            email = "ivan@example.com",
            role = "ADMIN",
            managerEmail = managerEmail
        ),
        asset = AssetSummary(
            id = 1L,
            name = "Parking Spot 10",
            category = CategorySummary(
                id = 3L,
                name = "Parking",
                bookingPeriod = "DAY",
                approval = true
            ),
            status = "ACTIVE",
            description = "Outdoor parking",
            location = "Level -2"
        ),
        status = "PENDING",
        bookingStart = "2026-01-04T09:00:00Z",
        bookingEnd = "2026-01-14T09:00:00Z",
        notes = "Some optional notes"
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
            status: String?,
            page: Int,
            size: Int
        ): BookingListResponse {
            getBookingsCalls++
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
            error("createBooking is not used in HomeViewModel tests.")
        }

        override suspend fun createRecurringBooking(request: RecurringBookingCreateRequest): List<BookingResponse> {
            error("createRecurringBooking is not used in HomeViewModel tests.")
        }

        override suspend fun approveBooking(bookingId: Long): BookingResponse {
            error("approveBooking is not used in HomeViewModel tests.")
        }

        override suspend fun rejectBooking(bookingId: Long): BookingResponse {
            error("rejectBooking is not used in HomeViewModel tests.")
        }

        override suspend fun updateBooking(
            bookingId: Long,
            request: BookingStatusUpdateRequest
        ): BookingResponse {
            error("updateBooking is not used in HomeViewModel tests.")
        }
    }

    private class FakeUserApi : UserApi {
        var response: UserResponse = buildDefaultUserResponse()
        var getUserException: Exception? = null

        override suspend fun getUserById(id: Long): UserResponse {
            getUserException?.let { throw it }
            return response
        }

        override suspend fun changePassword(id: Long, request: ChangePasswordRequest) {
            error("changePassword is not used in HomeViewModel tests.")
        }
    }

    companion object {
        private fun buildDefaultUserResponse() = UserResponse(
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
    }
}

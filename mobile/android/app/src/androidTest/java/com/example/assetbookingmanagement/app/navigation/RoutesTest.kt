package com.example.assetbookingmanagement.app.navigation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoutesTest {

    @Test
    fun testAssetDetailsBuildsConcreteRoute() {
        assertEquals("asset_details/15", Routes.assetDetails(assetId = 15))
    }

    @Test
    fun testApprovalRequestDetailsEncodesSharedTestData() {
        val route = Routes.approvalRequestDetails(
            bookingId = 21L,
            assetName = "Hp 15",
            requesterName = "Ivan Horvat",
            fromDate = "2026-01-04T09:00:00Z",
            toDate = "2026-01-14T09:00:00Z",
            status = "PENDING APPROVAL",
            isHourlyBooking = false
        )

        assertEquals(
            "approval_request_details/21?assetName=Hp%2015&requesterName=Ivan%20Horvat&fromDate=2026-01-04T09%3A00%3A00Z&toDate=2026-01-14T09%3A00%3A00Z&status=PENDING%20APPROVAL&isHourlyBooking=false",
            route
        )
    }

    @Test
    fun testBookingDetailsEncodesCategoryAndDateParameters() {
        val route = Routes.bookingDetails(
            bookingId = 7L,
            assetName = "Hp 15",
            fromDate = "2026-01-04T09:00:00Z",
            toDate = "2026-01-14T09:00:00Z",
            status = "PENDING",
            categoryName = "IT Equipment",
            isHourlyBooking = true
        )

        assertEquals(
            "booking_details/7?assetName=Hp%2015&fromDate=2026-01-04T09%3A00%3A00Z&toDate=2026-01-14T09%3A00%3A00Z&status=PENDING&categoryName=IT%20Equipment&isHourlyBooking=true",
            route
        )
    }

    @Test
    fun testCreateBookingBuildsConcreteRoute() {
        assertEquals("create_booking/10", Routes.createBooking(assetId = 10))
    }

    @Test
    fun testBookingSuccessEncodesAssetNameAndApprovalFlag() {
        val route = Routes.bookingSuccess(
            assetName = "Parking Spot 10",
            fromDate = "2026-01-04T09:00:00Z",
            toDate = "2026-01-14T09:00:00Z",
            approvalRequired = true
        )

        assertEquals(
            "booking_success?assetName=Parking%20Spot%2010&fromDate=2026-01-04T09%3A00%3A00Z&toDate=2026-01-14T09%3A00%3A00Z&approvalRequired=true",
            route
        )
    }
}

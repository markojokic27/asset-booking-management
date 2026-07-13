package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import constants.TestDates;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ApproveBookingSmokeTest extends BaseLogin {

    @Test
    public void successfulBookingMeetingRoomAddsEventToCalendar() {
        String fromDate = TestDates.approvalSmokeFrom();
        String toDate = TestDates.approvalSmokeTo();

        loginWithAndela();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION);

        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.enterToDate(toDate);
        bookingPage.clickCalendarDate(toDate);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());

        logoutPage.clickLogoutButton();
        waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION);
        loginWithManager();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.APPROVALS);
        approvalsPage.clickApproveInModal();

        logoutPage.clickLogoutButton();
        waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION);
        loginWithAndela();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.MY_BOOKING_URL);
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }
}
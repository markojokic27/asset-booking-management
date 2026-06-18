package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ApproveBookingSmokeTest extends BaseLogin {

    @Test
    public void successfulBookingMeetingRoomAddsEventToCalendar() {
        loginWithAndela();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION);

        bookingPage.clickitEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());

        logoutPage.clickLogoutButton();
        loginWithManager();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.APPROVALS);
        approvalsPage.clickApproveInModal();


        logoutPage.clickLogoutButton();
        loginWithAndela();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.MY_BOOKING_URL);
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }


}

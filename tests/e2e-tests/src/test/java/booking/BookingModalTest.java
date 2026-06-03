package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class BookingModalTest extends BaseLogin {

    @BeforeMethod
    public void setUp() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION);
    }


    @Test
    public void clickBookButtonNavigatesToAssetBookingsPage() {
        bookingPage.clickBookButton();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

    @Test
    public void bookNowIsDisabledWithoutSelectedDate() {
        bookingPage.clickBookButton();
        assertFalse(bookingPage.isBookAssetButtonEnabled());
    }

    @Test
    public void bookNowIsEnabledAfterSelectingFreeSlot() {
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.DATE_FUTURE);
        bookingPage.clickCalendarDate(CommonConstants.DATE_FUTURE);
        assertTrue(isElementVisible(bookingPage.bookAssetButton));
    }

    @Test
    public void bookButtonIsEnabledAfterSelectingDateOnHourlyAsset() {
        bookingPage.clickMeetingRoomCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.DATE_FUTURE);
        bookingPage.clickCalendarDate(CommonConstants.DATE_FUTURE);
        bookingPage.selectFromHour(CommonConstants.FROM_HOUR);
        bookingPage.selectToHour(CommonConstants.TO_HOUR);
        assertTrue(isElementVisible(bookingPage.bookAssetButton));
    }


    @Test
    public void successfulBookingLaptopAddsEventToCalendar(){
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE);
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookingMeetingRoomAddsEventToCalendar(){
        bookingPage.clickMeetingRoomCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE);
        bookingPage.selectFromHour(CommonConstants.FROM_HOUR);
        bookingPage.selectToHour(CommonConstants.TO_HOUR);
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void bookNowIsDisabledForInactiveAsset() {
        bookingPage.clickBookButtonForInactiveAsset();
        assertFalse(bookingPage.isBookAssetButtonEnabled());
    }
}

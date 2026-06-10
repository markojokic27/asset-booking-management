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
    public void bookNowIsEnabledAfterSelectingFreeSlot(){
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
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
    public void successfulBookingLaptopAddsEventToCalendar() {
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookingMeetingRoomAddsEventToCalendar(){
        bookingPage.clickMeetingRoomCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
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


    @Test
    public void successfulBookAssetButtonIsVisibleAfterSelectingRecurringDays(){
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.selectAllRecurringDays();
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());    }

    @Test
    public void successfulBookAssetAfterSelectingRecurringOneDays(){
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickCheckBoxDays();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void deselectRecurringDayRemovesBookButton(){
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.selectAllRecurringDays();
        bookingPage.selectAllRecurringDays();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void deselectRecurringDayRemovesBookButton1(){
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.selectAllRecurringDays();
        bookingPage.clickCheckBoxDays();
        assertTrue(bookingPage.isCalendarVisible());
    }

}

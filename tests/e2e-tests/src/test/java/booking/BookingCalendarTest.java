package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class BookingCalendarTest extends BaseLogin {

    @BeforeMethod
    public void setUpBookingPage() {
        login();
        getDriver().get(
                ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.ASSETS_URL + "/" + CommonConstants.BOOKED_ASSET_ID + CommonConstants.BOOKINGS_URL_EXTENSION);
    }

    @Test
    public void calendarIsVisibleOnPageLoad() {
        assertTrue(bookingPage.isCalendarVisible());
    }


    @Test
    public void clickFutureDateHighlightsCell() {
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        assertTrue(bookingPage.isCalendarCellSelected(CommonConstants.FUTURE_DATE_FROM));
    }

    @Test
    public void clickFutureDatePopulatesDateFilter() {
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        assertEquals(CommonConstants.FUTURE_DATE_FROM, bookingPage.getFromDateValue());
    }

    @Test
    public void nextMonthButtonChangesCalendarTitle(){
        bookingPage.clickNextMonth();
        assertTrue(isElementVisible(bookingPage.calendarTitle));
    }

    @Test
    public void prevMonthButtonNavigatesToPreviousMonth() {
        bookingPage.clickPrevMonth();
        assertTrue(isElementVisible(bookingPage.calendarTitle));
    }

}
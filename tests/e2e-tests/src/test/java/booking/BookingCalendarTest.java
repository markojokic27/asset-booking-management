package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import constants.TestDates;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class BookingCalendarTest extends BaseLogin {

    @BeforeMethod
    public void setUpBookingPage() {
        login();
        getDriver().get(
                ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.ASSETS_URL + "/" + CommonConstants.BOOKED_ASSET_ID + CommonConstants.BOOKINGS_URL_EXTENSION);
        isElementVisible(bookingPage.calendar);
    }

    @Test
    public void calendarIsVisibleOnPageLoad() {
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void clickFutureDateHighlightsCell() {
        String fromDate = TestDates.futureDateFrom();

        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        assertTrue(bookingPage.isCalendarCellSelected(fromDate));
    }

    @Test
    public void clickFutureDatePopulatesDateFilter(){
        String fromDate = TestDates.futureDateFrom();

        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        assertTrue(bookingPage.isCalendarCellSelected(fromDate));
    }

    @Test
    public void nextMonthButtonChangesCalendarTitle() {
        bookingPage.clickNextMonth();
        assertTrue(isElementVisible(bookingPage.calendarTitle));
    }

    @Test
    public void prevMonthButtonNavigatesToPreviousMonth() {
        bookingPage.clickPrevMonth();
        assertTrue(isElementVisible(bookingPage.calendarTitle));
    }
}
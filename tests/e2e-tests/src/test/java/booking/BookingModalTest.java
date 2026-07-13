package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import constants.TestDates;
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
        String fromDate = TestDates.futureDateFrom();
        String toDate = TestDates.futureDateTo();

        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(toDate);
        bookingPage.clickCalendarDate(toDate);
        assertTrue(isElementVisible(bookingPage.bookAssetButton));
    }

    @Test
    public void bookButtonIsEnabledAfterSelectingDateOnHourlyAsset() {
        String fromDate = TestDates.futureDateFrom();
        String toDate = TestDates.futureDateTo();

        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(toDate);
        bookingPage.clickCalendarDate(toDate);
        assertTrue(isElementVisible(bookingPage.bookAssetButton));
    }

    @Test
    public void successfulBookingLaptopAddsEventToCalendar() {
        String fromDate = TestDates.futureDateFrom();
        String toDate = TestDates.futureDateTo();

        bookingPage.clickLaptopCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(toDate);
        bookingPage.clickCalendarDate(toDate);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookingItEquipmentAddsEventToCalendar() {
        String fromDate = TestDates.futureDateFrom();
        String toDate = TestDates.futureDateTo();

        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(toDate);
        bookingPage.clickCalendarDate(toDate);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void cancelBookingItEquipmentAddsEventToCalendar() {
        String fromDate = TestDates.futureDateFrom();
        String toDate = TestDates.futureDateTo();

        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.clickNextMonth();
        bookingPage.clickCalendarDate(toDate);
        bookingPage.clickBookAssetButton();
        bookingPage.clickCancelBookButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void bookNowIsDisabledForInactiveAsset() {
        bookingPage.clickBookButtonForInactiveAsset();
        assertFalse(bookingPage.isBookAssetButtonEnabled());
    }

    @Test
    public void successfulBookAssetButtonIsVisibleAfterSelectingRecurringDays() {
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickCheckBoxDays();
        bookingPage.selectAllRecurringDays();
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookAssetAfterSelectingRecurringOneDays() {
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickCheckBoxDays();
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void deselectRecurringDayRemovesBookButton() {
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.selectAllRecurringDays();
        bookingPage.selectAllRecurringDays();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void deselectRecurringDayRemovesBookButton1() {
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.selectAllRecurringDays();
        bookingPage.clickCheckBoxDays();
        assertTrue(bookingPage.isCalendarVisible());
    }
}
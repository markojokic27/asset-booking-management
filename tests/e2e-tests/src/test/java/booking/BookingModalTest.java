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
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        assertTrue(isElementVisible(bookingPage.bookAssetButton));
    }

    @Test
    public void bookButtonIsEnabledAfterSelectingDateOnHourlyAsset() {
        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        assertTrue(isElementVisible(bookingPage.bookAssetButton));
    }

    @Test
    public void successfulBookingLaptopAddsEventToCalendar() {
        bookingPage.clickLaptopCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookingItEquipmentAddsEventToCalendar() {
        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void cancelBookingItEquipmentAddsEventToCalendar() {
        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickNextMonth();
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
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
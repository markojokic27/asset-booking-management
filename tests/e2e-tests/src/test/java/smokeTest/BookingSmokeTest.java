package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import constants.TestDates;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class BookingSmokeTest extends BaseLogin {

    @BeforeMethod
    public void setUp() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION);
    }

    @Test
    public void successfulBookingLaptopAddsEventToCalendar() {
        String fromDate = TestDates.smokeDateFrom();
        String toDate = TestDates.smokeDateTo();

        bookingPage.clickLaptopCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.enterToDate(toDate);
        bookingPage.clickCalendarDate(toDate);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookingItEquipmentAddsEventToCalendar() {
        String fromDate = TestDates.smokeDateFrom();
        String toDate = TestDates.smokeDateTo();

        bookingPage.clickItEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(fromDate);
        bookingPage.clickCalendarDate(fromDate);
        bookingPage.enterToDate(toDate);
        bookingPage.clickCalendarDate(toDate);
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void bookNowIsDisabledForInactiveAsset() {
        bookingPage.clickBookButtonForInactiveAsset();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    @Test
    public void clickParkingMapButtonOpenModal() {
        bookingPage.clickParkingCategory();
        bookingPage.clickParkingMapButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    @Test
    public void parkingMapModalShowsLevelMinus1ByDefault() {
        bookingPage.clickParkingCategory();
        bookingPage.clickParkingMapButton();
        assertTrue(isElementVisible(bookingPage.floorLevelMinus1Active));
    }

    @Test
    public void selectDateClickSpotAndBookLevel1() {
        bookingPage.clickParkingMapButton();
        bookingPage.selectParkingMapDate(TestDates.parkingSmoke());
        int freeSpot = bookingPage.getFirstAvailableParkingSpot();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
    }

    @Test
    public void successfulBookAssetButtonIsVisibleAfterSelectingRecurringDays() {
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickCalendarDate(TestDates.smokeDateFrom());
        bookingPage.clickCheckBoxDays();
        bookingPage.selectAllRecurringDays();
        bookingPage.clickBookAssetButton();
        bookingPage.clickBookNowButton();
        assertTrue(bookingPage.isCalendarVisible());
    }
}
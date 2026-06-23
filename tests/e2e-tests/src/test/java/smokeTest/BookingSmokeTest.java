package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
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
        bookingPage.clickParkingCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.getFutureDateFrom());
        bookingPage.clickCalendarDate(CommonConstants.getFutureDateFrom());
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(CommonConstants.getFutureDateTo());
        bookingPage.clickCalendarDate(CommonConstants.getFutureDateTo());
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookingItEquipmentAddsEventToCalendar() {
        bookingPage.clickitEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.getFutureDateFrom());
        bookingPage.clickCalendarDate(CommonConstants.getFutureDateFrom());
        bookingPage.clickBookAssetButton();
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
        bookingPage.clickParkingMapButton();
        assertTrue(isElementVisible(bookingPage.floorLevelMinus1Active));
    }

    @Test
    public void selectDateClickSpotAndBookLevel1() {
        bookingPage.clickParkingMapButton();
        bookingPage.selectParkingMapDate(CommonConstants.getParkingTestDate());
        int freeSpot = bookingPage.getFirstAvailableParkingSpot();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
    }
}
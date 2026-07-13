package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import constants.TestDates;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;


public class ParkingMapTest extends BaseLogin {

    @BeforeMethod
    public void setUp() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION);
        bookingPage.clickParkingCategory();
    }

    @Test
    public void clickParkingMapButtonOpenModal() {
        bookingPage.clickParkingMapButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    @Test
    public void parkingMapModalShowsLevelMinus1ByDefault() {
        bookingPage.clickParkingMapButton();
        assertTrue(isElementVisible(bookingPage.floorLevelMinus1Active));
    }

    @Test
    public void clickLevelMinus2SwitchesFloor() {
        bookingPage.clickParkingMapButton();
        bookingPage.clickFloorLevel(CommonConstants.FLOOR_LEVEL_MINUS_2);
        assertTrue(isElementVisible(bookingPage.floorLevelMinus2Active));
    }

    @Test
    public void clickLevelMinus1AfterMinus2SwitchesBack() {
        bookingPage.clickParkingMapButton();
        bookingPage.clickFloorLevel(CommonConstants.FLOOR_LEVEL_MINUS_2);
        bookingPage.clickFloorLevel(CommonConstants.FLOOR_LEVEL_MINUS_1);
        assertTrue(isElementVisible(bookingPage.floorLevelMinus1Active));
    }

    @Test
    public void closeModalWithCloseButton() {
        bookingPage.clickParkingMapButton();
        bookingPage.closeParkingMapModal();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    @Test
    public void selectDateClickSpotAndBookLevel1() {
        bookingPage.clickParkingMapButton();
        bookingPage.selectParkingMapDate(TestDates.parkingTestDate());
        int freeSpot = bookingPage.getFirstAvailableParkingSpot();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
    }

    @Test
    public void selectDateClickSpotAndBookLevel2() {
        bookingPage.clickParkingMapButton();
        bookingPage.clickFloorLevel(CommonConstants.FLOOR_LEVEL_MINUS_2);
        bookingPage.selectParkingMapDate(TestDates.parkingTestDate());
        int freeSpot = bookingPage.getFirstAvailableParkingSpot();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
    }

    @Test
    public void clickTakenSpotShowsTakenStatus() {
        bookingPage.clickParkingMapButton();
        bookingPage.clickFloorLevel(CommonConstants.FLOOR_LEVEL_MINUS_2);
        bookingPage.selectParkingMapDate(TestDates.parkingTestDate());
        int freeSpot = bookingPage.getFirstAvailableParkingSpot();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(freeSpot);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
        assertFalse(isElementEnabled(bookingPage.spotPopoverBookButton));
    }
}
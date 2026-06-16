package booking;
import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
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
        assertTrue(isElementVisible(bookingPage.parkingMapModal));
    }

    @Test
    public void parkingMapModalShowsLevelMinus1ByDefault() {
        bookingPage.clickParkingMapButton();
        assertTrue(isElementVisible(bookingPage.floorLevelMinus1Active));
    }

    @Test
    public void clickLevelMinus2SwitchesFloor(){
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
    public void selectDateClickSpotAndBook() {
        bookingPage.clickParkingMapButton();
        bookingPage.selectParkingMapDate(CommonConstants.PARKING_TEST_DATE);
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER_LEVEL);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
    }

    @Test
    public void selectDateClickSpotAndBookLevel() throws InterruptedException{
        bookingPage.clickParkingMapButton();
        bookingPage.clickFloorLevel(CommonConstants.FLOOR_LEVEL_MINUS_2);
        bookingPage.selectParkingMapDate(CommonConstants.PARKING_TEST_DATE);
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER_LEVEL);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER_LEVEL);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        Thread.sleep(2000);
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
    }

    @Test
    public void clickTakenSpotShowsTakenStatus() {
        bookingPage.clickParkingMapButton();
        bookingPage.clickFloorLevel(CommonConstants.FLOOR_LEVEL_MINUS_2);
        bookingPage.selectParkingMapDate(CommonConstants.PARKING_TEST_DATE);
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER_LEVEL);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
        assertFalse(isElementEnabled(bookingPage.spotPopoverBookButton));
    }

    @Test
    public void closeSpotPopoverWithXButton() {
        bookingPage.clickParkingMapButton();
        bookingPage.selectParkingMapDate(CommonConstants.PARKING_TEST_DATE);
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER121);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.closeSpotPopover();
        assertFalse(isElementVisible(bookingPage.spotPopoverCloseButton));
    }
}
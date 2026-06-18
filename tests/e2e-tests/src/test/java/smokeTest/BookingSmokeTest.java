package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;
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
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickNextMonth();
        bookingPage.enterToDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_TO);
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void successfulBookingItEquipmentAddsEventToCalendar() {
        bookingPage.clickitEquipmentCategory();
        bookingPage.clickBookButton();
        bookingPage.clickNextMonth();
        bookingPage.clickNextMonth();
        bookingPage.enterFromDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickCalendarDate(CommonConstants.FUTURE_DATE_FROM);
        bookingPage.clickBookAssetButton();
        assertTrue(bookingPage.isCalendarVisible());
    }

    @Test
    public void bookNowIsDisabledForInactiveAsset() {
        bookingPage.clickBookButtonForInactiveAsset();
        assertFalse(bookingPage.isBookAssetButtonEnabled());
    }

    @Test
    public void clickParkingMapButtonOpenModal() {
        bookingPage.clickParkingCategory();

        bookingPage.clickParkingMapButton();
        assertTrue(isElementVisible(bookingPage.parkingMapModal));
    }

    @Test
    public void parkingMapModalShowsLevelMinus1ByDefault() {
        bookingPage.clickParkingMapButton();
        assertTrue(isElementVisible(bookingPage.floorLevelMinus1Active));
    }

    @Test
    public void selectDateClickSpotAndBookLevel1() {
        bookingPage.clickParkingMapButton();
        bookingPage.selectParkingMapDate(CommonConstants.PARKING_TEST_DATE);
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        bookingPage.clickSpotBookButton();
        bookingPage.clickParkingSpot(CommonConstants.PARKING_SPOT_NUMBER_LEVEL);
        assertTrue(isElementVisible(bookingPage.spotPopover));
        assertTrue(elementHasClass(bookingPage.parkingSpotStatus, "bg-orange-100"));
    }


}

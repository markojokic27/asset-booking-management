import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import constants.TestDates;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class AllBookingTest extends BaseLogin {

    @BeforeMethod
    public void setUpBookingPage() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.MY_BOOKING_URL);
    }

    @Test
    public void viewMyBookingPage() {
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }

    @Test
    public void searchBookingsFiltersTable() {
        myBookingsPage.searchAssets(CommonConstants.SEARCH_ASSET);
        assertTrue(waitForUrlContains(CommonConstants.MY_BOOKING_URL));
    }

    @Test
    public void selectAssetFilter() {
        myBookingsPage.selectAssetFilter(CommonConstants.ASSET);
        assertTrue(waitForUrlContains(CommonConstants.MY_BOOKING_URL));
    }

    @Test
    public void filterBookingsByDateRange() {
        myBookingsPage.inputFromDate(TestDates.futureDateFrom());
        myBookingsPage.inputToDate(TestDates.futureDateTo());
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }

    @Test
    public void filterBookingsByFromDateOnly() {
        myBookingsPage.inputFromDate(TestDates.futureDateFrom());
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }

    @Test
    public void filterBookingsByToDateOnly()  {
        bookingPage.clickNextMonth();
        myBookingsPage.inputToDate(TestDates.futureDateTo());
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }

    @Test
    public void cancelBookingConfirm() {
        myBookingsPage.clickFirstCancelButton();
        assertTrue(isElementVisible(myBookingsPage.cancelBookingModal));
        myBookingsPage.confirmCancel();
        assertFalse(isElementVisible(myBookingsPage.cancelBookingModal));
    }

    @Test
    public void cancelBookingKeep(){
        myBookingsPage.clickFirstCancelButton();
        assertTrue(isElementVisible(myBookingsPage.cancelBookingModal));
        myBookingsPage.keepBooking();
        assertFalse(isElementVisible(myBookingsPage.cancelBookingModal));
    }

    @Test
    public void selectStatusFilter(){
        myBookingsPage.selectMyBookingStatus(CommonConstants.MY_BOOKING_STATUS);
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }


    @Test
    public void selectAssetFilterField(){
        myBookingsPage.selectMyBookingAsset(CommonConstants.ASSETS);
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }
}
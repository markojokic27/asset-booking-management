import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
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
        myBookingsPage.inputFromDate(CommonConstants.getDateFrom());
        myBookingsPage.inputToDate(CommonConstants.getDateTo());
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }

    @Test
    public void filterBookingsByFromDateOnly() {
        myBookingsPage.inputFromDate(CommonConstants.getDateFrom());
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }

    @Test
    public void filterBookingsByToDateOnly()  {
        myBookingsPage.inputToDate(CommonConstants.getDateTo());
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
}
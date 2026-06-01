package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class BookingSearchTest extends BaseLogin {

    @BeforeMethod
    public void setUpBookingPage() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION
        );
    }

    @Test
    public void searchAssetsFiltersTable() {
        bookingPage.searchAssets(CommonConstants.SEARCH_ASSET);
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    @Test
    public void resetFiltersAfterSearchClearsInput() {
        bookingPage.searchAssets(CommonConstants.SEARCH_ASSET);
        bookingPage.clickResetFilters();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }
}
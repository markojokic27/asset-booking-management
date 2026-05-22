package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class BookingSearchTest extends BaseLogin {

    @Test
    public void searchAssets(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.BOOKINGS_URL_EXTENSION));
        bookingPage.searchAssets(CommonConstants.SEARCH_ASSET);
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));

    }
}

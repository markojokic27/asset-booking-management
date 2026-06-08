import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AllBookingTest extends BaseLogin {

    @Test
    public void viewMyBookingPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.MY_BOOKING_URL));
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }
}

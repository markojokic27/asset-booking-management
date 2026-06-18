package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ViewBookingsSmokeTest extends BaseLogin {

    @BeforeMethod
    public void setUpBookingPage() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.MY_BOOKING_URL
        );
    }

    @Test
    public void viewMyBookingPage(){
        assertTrue(isElementVisible(myBookingsPage.bookingList));
    }
}

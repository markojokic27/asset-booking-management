package booking;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;


public class BookingModalTest extends BaseLogin {

    @BeforeMethod
    public void setUpBookingPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    @Test
    public void ClickForBookAsset() throws InterruptedException{
        bookingPage.clickBookButton();
        Thread.sleep(2000);
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

}

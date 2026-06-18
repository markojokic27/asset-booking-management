package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class LogoutSmokeTest extends BaseLogin {

    @BeforeMethod
    public void setUpLogoutPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    @Test
    public void logout(){
        logoutPage.clickLogoutButton();

        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));
    }
}

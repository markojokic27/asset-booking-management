package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class LoginSmokeTest extends BaseLogin {

    @Test
    void UserCanLogin() {
        login();
        assertEquals(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION, getUrl());
    }
}

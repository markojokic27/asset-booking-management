
package auth;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class LoginTest extends BaseLogin {


    @Test
    void UserCanLogin() {
        login();
        assertEquals(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.BOOKINGS_URL_EXTENSION, getUrl());
    }

    @Test
    void loginWithEmptyUsername() {
        loginPage.login(
                "",
                CommonConstants.ADMIN_USERNAME
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));
    }

    @Test
    void loginWithEmptyPassword(){
        loginPage.login(
                CommonConstants.ADMIN_PASS,
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));
    }

    @Test
    void loginWithEmptyAllFields(){
        loginPage.login(
                "",
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));
    }

    @Test
    void loginWithWrongUsername(){
        loginPage.login(
                CommonConstants.WRONG_USERNAME,
                CommonConstants.ADMIN_USERNAME
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));
    }

    @Test
    void loginWithWrongPassword(){
        loginPage.login(
                CommonConstants.ADMIN_USERNAME,
                CommonConstants.WRONG_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));
    }


}
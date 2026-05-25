package playwright;

import constants.CommonConstants;
import org.junit.jupiter.api.Test;
import playwright.base.BaseTest;

public class TestLogin extends BaseTest {

    @Test
    void userCanLogin(){
        loginPage.login(CommonConstants.ADMIN_USERNAME, CommonConstants.ADMIN_PASS);
        loginPage.assertOnBookingsPage();
    }

    @Test
    void loginWithEmptyUsername() {
        loginPage.login("", CommonConstants.ADMIN_PASS);
        loginPage.assertOnLoginPage();
    }

    @Test
    void loginWithEmptyPassword() {
        loginPage.login(CommonConstants.ADMIN_USERNAME, "");
        loginPage.assertOnLoginPage();
    }

    @Test
    void loginWithEmptyAllFields() {
        loginPage.login("", "");
        loginPage.assertOnLoginPage();
    }

    @Test
    void loginWithWrongUsername() {
        loginPage.login(CommonConstants.WRONG_USERNAME, CommonConstants.ADMIN_PASS);
        loginPage.assertOnLoginPage();
    }

    @Test
    void loginWithWrongPassword() {
        loginPage.login(CommonConstants.ADMIN_USERNAME, CommonConstants.WRONG_PASSWORD);
        loginPage.assertOnLoginPage();
    }
}
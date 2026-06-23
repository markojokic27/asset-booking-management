package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ChangePasswordSmokeTest extends BaseLogin {

    @Test
    void changeAccountPasswordWithValidData(){
        loginWithEmployee();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ACCOUNT_INFO));

        accountPage.openHeadingModal();

        accountPage.account(
                CommonConstants.PASSWORD,
                CommonConstants.NEW_PASSWORD,
                CommonConstants.NEW_PASSWORD
        );

        assertTrue(waitForUrlContains(CommonConstants.ACCOUNT_INFO));
        logoutPage.clickLogoutButton();
        loginPage.login(
                CommonConstants.EMPLOYEE_USERNAME,
                CommonConstants.NEW_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));

        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ACCOUNT_INFO));
        accountPage.openHeadingModal();

        accountPage.account(
                CommonConstants.NEW_PASSWORD,
                CommonConstants.PASSWORD,
                CommonConstants.PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.ACCOUNT_INFO));


    }
}

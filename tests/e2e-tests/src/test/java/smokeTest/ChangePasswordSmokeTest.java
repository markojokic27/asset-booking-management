package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ChangePasswordSmokeTest extends BaseLogin {

    @Test
    void changeAccountPasswordWithValidData(){
        loginWithAndela();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ACCOUNT_INFO));

        accountPage.openHeadingModal();

        accountPage.account(
                CommonConstants.PASS,
                CommonConstants.NEW_PASS,
                CommonConstants.NEW_PASS
        );
        logoutPage.clickLogoutButton();
        loginPage.login(
                CommonConstants.USERNAME,
                CommonConstants.PASS
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));

    }
}

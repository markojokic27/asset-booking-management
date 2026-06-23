import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AccountInfoTest extends BaseLogin {

    @BeforeMethod
    public void setUpAccountPage(){
        loginWithEmployee();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ACCOUNT_INFO));

        accountPage.openHeadingModal();
    }


    @Test
    void changeAccountPasswordWithValidData(){
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

    @Test
    void changePasswordModalStaysOpenWhenCurrentPasswordIsEmpty() {
        accountPage.account(
                "",
                CommonConstants.NEW_PASSWORD,
                CommonConstants.NEW_PASSWORD
        );

        assertTrue(waitForUrlContains(CommonConstants.ACCOUNT_INFO));
    }

    @Test
    void changePasswordModalStaysOpenWhenNewPasswordIsEmpty(){
        accountPage.account(
                CommonConstants.PASSWORD,
                "",
                CommonConstants.NEW_PASSWORD
        );

        assertTrue(isElementVisible(accountPage.typeCurrentPassword));
    }

    @Test
    void changePasswordModalStaysOpenWhenConfirmPasswordIsEmpty() {
        accountPage.account(
                CommonConstants.PASSWORD,
                CommonConstants.NEW_PASSWORD,
                ""
        );

        assertTrue(isElementVisible(accountPage.typeCurrentPassword));
    }

    @Test
    void changePasswordModalStaysOpenWhenAllFieldIsEmpty() {
        accountPage.account(
                "",
                "",
                ""
        );

        assertTrue(isElementVisible(accountPage.typeCurrentPassword));
    }

    @Test
    void changePasswordModalStaysOpenWithInvalidCurrentPassword() {
        accountPage.account(
                CommonConstants.WRONG_PASSWORD,
                CommonConstants.NEW_PASSWORD,
                CommonConstants.NEW_PASSWORD
        );

        assertTrue(isElementVisible(accountPage.typeCurrentPassword));
    }

    @Test
    void changePasswordModalStaysOpenWithLongNewPassword() {
        accountPage.account(
                CommonConstants.PASSWORD,
                CommonConstants.LONG_PASSWORD,
                CommonConstants.NEW_PASSWORD
        );

        assertTrue(isElementVisible(accountPage.typeCurrentPassword));
    }

    @Test
    void changePasswordModalStaysOpenWithShortNewPassword() {
        accountPage.account(
                CommonConstants.PASSWORD,
                CommonConstants.SHORT_PASSWORD,
                CommonConstants.NEW_PASSWORD
        );

        assertTrue(isElementVisible(accountPage.typeCurrentPassword));
    }

    @Test
    void changePasswordModalStaysOpenWithDifferentConfirmPassword() {
        accountPage.account(
                CommonConstants.PASSWORD,
                CommonConstants.LONG_PASSWORD,
                CommonConstants.PASSWORD
        );

        assertTrue(isElementVisible(accountPage.typeCurrentPassword));
    }
}

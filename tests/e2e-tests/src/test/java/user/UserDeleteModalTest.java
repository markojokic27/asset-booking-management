package user;
import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserDeleteModalTest extends BaseLogin {

    @BeforeMethod
    void setupUserDeletePage() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.USERS_URL);
        userPage.userOpenDeleteModalByUsername(CommonConstants.DELETE_USER_USERNAME);
    }

    @Test
    void userDeleteModalClosesOnCancelButton() {
        userPage.userDeleteCancel();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userDeleteModalConfirmDeletesUser() {
        userPage.userDeleteConfirm();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }
}
package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserSmokeTest extends BaseLogin {

    @BeforeMethod
    void setupUserPage() {
        login();

        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.USERS_URL);

        userPage.userOpenModal();
    }

    //Create user with valid data

    @Test
    void userCreateModalSavesValidUser() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

}

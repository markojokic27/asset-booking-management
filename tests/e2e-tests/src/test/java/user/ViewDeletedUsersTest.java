package user;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ViewDeletedUsersTest extends BaseLogin {

    @Test
    public void viewDeletedUsers(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.USERS_URL));
        userPage.clickToggleDeletedUsers();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));

    }
}

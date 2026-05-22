package user;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserSearchTest extends BaseLogin {

    @Test
    public void searchUsers() throws InterruptedException{
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.USERS_URL));
        userPage.searchUsers(CommonConstants.SEARCH_USERS);
        Thread.sleep(2000);
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));

    }
}

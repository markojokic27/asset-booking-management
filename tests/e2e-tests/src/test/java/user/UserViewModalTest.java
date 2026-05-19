package user;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserViewModalTest extends BaseLogin {

    @BeforeMethod
    void setUpUserPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.USERS_URL));

        userPage.userViewOpenModal();
    }

    @Test
    void userViewModal(){

        assertTrue(isElementVisible(userPage.userViewOpenModal));
    }

    @Test
    public void userViewModalCloseOnCloseButton(){
        userPage.userViewCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }
}

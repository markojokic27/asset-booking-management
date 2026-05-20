package user;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserBookingModalTest extends BaseLogin {

    @BeforeMethod
    public void setUpUserPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.USERS_URL));

        userPage.userBookingOpenModal();
    }

    @Test
    public void userBookingModalCloseOnCloseButton(){
        userPage.userBookingCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

}

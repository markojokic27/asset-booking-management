package user;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserSearchTest extends BaseLogin {

    @BeforeMethod
    public void setUpUser(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.USERS_URL));

    }

    @Test
    public void searchUserWithName(){
        userPage.searchUsers(CommonConstants.SEARCH_USERS);
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }


    @Test
    public void searchUserWithRole(){
        userPage.selectUserRole(CommonConstants.SEARCH_ROLE);
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    public void selectUserStatus(){
        userPage.selectUserDepartment(CommonConstants.SEARCH_DEPARTMENT);
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    public void clickShowDeleted() {
        userPage.clickShowDelete();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }





}

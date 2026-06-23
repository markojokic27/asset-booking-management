package baselogin;

import config.ConfigFromFile;
import constants.CommonConstants;
import factory.PageAndHandlerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertTrue;

public class BaseLogin extends PageAndHandlerFactory {

    @BeforeClass
    public void setUpBeforeTestClass() {

        assertTrue(openBrowser());
        setupPagesAndHandlers();
    }

    @BeforeMethod
    public void setUpBeforeEachTest() {
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.LOGIN_URL_EXTENSION);
    }


    @AfterClass
    public void tearDownAfterTestClass() {

        closeBrowser();
    }

    protected void login() {
        loginPage.typeUsername(CommonConstants.ADMIN_USERNAME);
        loginPage.typePassword(CommonConstants.ADMIN_PASS);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    protected void loginWithEmployee() {
        loginPage.typeUsername(CommonConstants.EMPLOYEE_USERNAME);
        loginPage.typePassword(CommonConstants.PASSWORD);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    protected void loginWithManager() {
        loginPage.typeUsername(CommonConstants.MANAGER_USERNAME);
        loginPage.typePassword(CommonConstants.MANAGER_PASS);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    protected void loginWithAndela() {
        loginPage.typeUsername(CommonConstants.USERNAME);
        loginPage.typePassword(CommonConstants.PASS);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }
}

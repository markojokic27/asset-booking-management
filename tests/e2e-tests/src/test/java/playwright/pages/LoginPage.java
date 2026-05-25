package playwright.pages;

import com.microsoft.playwright.Page;
import constants.CommonConstants;
import playwright.commonmethods.CommonMethods;

public class LoginPage extends CommonMethods {

    private static final String LOGIN_BUTTON   = "[data-testid='login-button']";
    private static final String USERNAME_FIELD = "[data-testid='username']";
    private static final String PASSWORD_FIELD = "[data-testid='password']";

    public LoginPage(Page page) {
        super(page);
    }

    public void clickLoginButton() {
        clickOnElement(LOGIN_BUTTON);
    }

    public void typePassword(String password) {
        typeInElement(PASSWORD_FIELD, password);
    }

    public void typeUsername(String username) {
        typeInElement(USERNAME_FIELD, username);
    }

    public void login(String username, String password) {
        typeUsername(username);
        typePassword(password);
        clickLoginButton();
    }

    public void assertOnLoginPage() {
        waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION);
    }

    public void assertOnBookingsPage() {
        waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION);
    }
}
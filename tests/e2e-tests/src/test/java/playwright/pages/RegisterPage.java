package playwright.pages;

import com.microsoft.playwright.Page;
import constants.CommonConstants;
import playwright.commonmethods.CommonMethods;

public class RegisterPage extends CommonMethods {

    private static final String NAME_FIELD = "[data-testid='name']";
    private static final String SURNAME_FIELD  = "[data-testid='surname']";
    private static final String USERNAME_FIELD = "[data-testid='username']";
    private static final String PASSWORD_FIELD = "[data-testid='password']";
    private static final String SUBMIT_BUTTON  = "[data-testid='register-button']";

    public RegisterPage(Page page) {
        super(page);
    }

    public void typeName(String name) {
        typeInElement(NAME_FIELD, name);
    }

    public void typeSurname(String surname) {
        typeInElement(SURNAME_FIELD, surname);
    }

    public void typeUsername(String username) {
        typeInElement(USERNAME_FIELD, username);
    }
    public void typePassword(String password) {
        typeInElement(PASSWORD_FIELD, password);
    }

    public void clickSubmit() {
        clickOnElement(SUBMIT_BUTTON);
    }

    public void register(String name, String surname, String username, String password) {
        typeName(name);
        typeSurname(surname);
        typeUsername(username);
        typePassword(password);
        clickSubmit();
    }

    public void assertOnRegisterPage() {
        waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION);
    }

    public void assertOnLoginPage() {
        waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION);
    }
}
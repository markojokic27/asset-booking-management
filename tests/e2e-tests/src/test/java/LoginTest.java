import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions. *;

public class LoginTest extends BaseTest {

    private static final String WRONG_USERNAME="user_admin!";
    private static final String WRONG_PASSWORD="pass.1234";
    private static final By USERNAME_INPUT = By.cssSelector("[data-testid='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-testid='password']");
    private static final By LOGIN_BUTTON   = By.cssSelector("[data-testid='login-button']");

    private void assertStaysOnLogin() {
        wait.until(ExpectedConditions.urlToBe(LOGIN_URL));
        assertEquals(LOGIN_URL, driver.getCurrentUrl());
    }

    @BeforeEach
    void navigateToLogin(){
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_INPUT));
    }

    @Test
    void userCanLogin() {
        login();
        wait.until(ExpectedConditions.urlToBe(POST_LOGIN_URL));
        assertEquals(POST_LOGIN_URL, driver.getCurrentUrl());
    }

    @Test
    void loginWithEmptyUsername() {
        driver.findElement(PASSWORD_INPUT).sendKeys(LOGIN_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
        assertStaysOnLogin();
    }

    @Test
    void loginWithEmptyPassword() {
        driver.findElement(USERNAME_INPUT).sendKeys(LOGIN_USERNAME);
        driver.findElement(LOGIN_BUTTON).click();
        assertStaysOnLogin();
    }

    @Test
    void loginWithBothFieldsEmpty() {
        driver.findElement(LOGIN_BUTTON).click();
        assertStaysOnLogin();
    }

    @Test
    void loginWithIncorrectUsername() {
        driver.findElement(USERNAME_INPUT).sendKeys(WRONG_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(LOGIN_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
        assertStaysOnLogin();
    }

    @Test
    void loginWithIncorrectPassword() {
        driver.findElement(USERNAME_INPUT).sendKeys(LOGIN_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(WRONG_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
        assertStaysOnLogin();
    }

}
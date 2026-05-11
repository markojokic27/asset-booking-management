import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions. *;

public class LoginTest extends BaseTest {

    private static final String INVALID_USERNAME="user_admin!";
    private static final String INVALID_PASSWORD="pass.1234";
    private static final By USERNAME_INPUT = By.cssSelector("[data-testid='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-testid='password']");
    private static final By LOGIN_BUTTON   = By.cssSelector("[data-testid='login-button']");

    private void assertStaysOnLogin() {
        wait.until(ExpectedConditions.urlContains("/login"));
        String currentUrl = driver.getCurrentUrl();
        assertNotNull(currentUrl);
        assertTrue(currentUrl.contains("/login"));
    }

    @BeforeEach
    void navigateToLogin(){
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_INPUT));
    }

    @Test
    void UserCanLogin() {
        login(); 
        assertEquals(POST_LOGIN_URL, driver.getCurrentUrl());
    }

    @Test
    void LoginWithEmptyUsername() {
        driver.findElement(PASSWORD_INPUT).sendKeys(LOGIN_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertStaysOnLogin();
    }

    @Test
    void LoginWithEmptyPassword() {
        driver.findElement(USERNAME_INPUT).sendKeys(LOGIN_USERNAME);
        driver.findElement(LOGIN_BUTTON).click();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertStaysOnLogin();
    }

    @Test
    void LoginWithBothFieldsEmpty() {
        driver.findElement(LOGIN_BUTTON).click();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertStaysOnLogin();
    }

    @Test
    void LoginWithIncorrectUsername() {
        driver.findElement(USERNAME_INPUT).sendKeys(INVALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(LOGIN_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertStaysOnLogin();
    }

    @Test
    void LoginWithIncorrectPassword() {
        driver.findElement(USERNAME_INPUT).sendKeys(LOGIN_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(INVALID_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertStaysOnLogin();
    }

}
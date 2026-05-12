import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
 import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions. *;

public class RegisterTest extends BaseTest{

    private static final String REGISTER_URL = "http://localhost:5173/register";
    private static final String VALID_NAME="Ivan";
    private static final String VALID_USERNAME="ivanivic";
    private static final String VALID_SURNAME="Ivic";
    private static final String VALID_PASSWORD="password.123";
    private static final String INVALID_NAME="Ivan!";
    private static final String INVALID_USERNAME="ivanivic?";
    private static final String INVALID_SURNAME="Ivic!";
    private static final String SHORT_PASSWORD="pas123";
    private static final String LONG_PASSWORD="p".repeat(51);
    private static final String SHORT_NAME="i";
    private static final String LONG_NAME="i".repeat(101);
    private static final String SHORT_SURNAME="i";
    private static final String LONG_SURNAME="i".repeat(101);
    private static final String SHORT_USERNAME="i";
    private static final String LONG_USERNAME="i".repeat(51);

    private static final By NAME_INPUT = By.cssSelector("[data-testid='name']");
    private static final By SURNAME_INPUT = By.cssSelector("[data-testid='surname']");
    private static final By USERNAME_INPUT = By.cssSelector("[data-testid='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-testid='password']");
    private static final By REGISTER_BUTTON   = By.cssSelector("[data-testid='register-button']");

    @BeforeEach
    void navigateToRegister(){
        driver.get(REGISTER_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(NAME_INPUT));
    }

    private void assertStaysOnRegister() {
        wait.until(ExpectedConditions.urlToBe(REGISTER_URL));
        assertEquals(REGISTER_URL, driver.getCurrentUrl());
    }

    // Register with valid data

    @Test
    void userCanRegister() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        wait.until(ExpectedConditions.urlToBe(LOGIN_URL));
        assertEquals(LOGIN_URL, driver.getCurrentUrl());
    }

    // Register with empty fields

    @Test
    void registerWithEmptyName() {
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithEmptySurname() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithEmptyUsername() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithEmptyPassword() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithAllFieldsEmpty() {
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    // Register with invalid data

    @Test
    void registerWithInvalidName() {
        driver.findElement(NAME_INPUT).sendKeys(INVALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithInvalidSurname() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(INVALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithInvalidUsername() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(INVALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    // Register with short fields

    @Test
    void registerWithShortName() {
        driver.findElement(NAME_INPUT).sendKeys(SHORT_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithShortSurname() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(SHORT_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithShortUsername() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(SHORT_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithShortPassword() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(SHORT_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }


    // Register with long fields

    @Test
    void registerWithLongName() {
        driver.findElement(NAME_INPUT).sendKeys(LONG_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithLongSurname() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(LONG_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithLongUsername() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(LONG_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }

    @Test
    void registerWithLongPassword() {
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(LONG_PASSWORD);
        driver.findElement(REGISTER_BUTTON).click();
        assertStaysOnRegister();
    }
}
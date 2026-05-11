import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AddUserTest extends BaseTest {

    private static final String USERS_URL="http://localhost:5173/users";
    private static final String VALID_NAME="Ivan";
    private static final String VALID_USERNAME="ivanivic";
    private static final String VALID_SURNAME="Ivic";
    private static final String VALID_EMAIL="ivanivic@example.com";
    private static final String VALID_PASSWORD="password.123";
    private static final String VALID_MANAGER_EMAIL="manager@example.com";
    private static final String VALID_NOTES="This is a dummy Ivan account";
    private static final String INVALID_NAME="Ivan!";
    private static final String INVALID_USERNAME="ivanivic?";
    private static final String INVALID_SURNAME="Ivic!";
    private static final String INVALID_EMAIL="ivanivic";
    private static final String INVALID_MANAGER_EMAIL="manager";
    private static final String SHORT_PASSWORD="pas123";
    private static final String LONG_PASSWORD="p".repeat(51);
    private static final String SHORT_NAME="i";
    private static final String LONG_NAME="i".repeat(101);
    private static final String SHORT_SURNAME="i";
    private static final String LONG_SURNAME="i".repeat(101);
    private static final String SHORT_USERNAME="i";
    private static final String LONG_USERNAME="i".repeat(51);
    private static final String LONG_EMAIL="i".repeat(255);
    private static final String LONG_MANAGER_EMAIL="i".repeat(255);
    private static final String LONG_NOTES="i".repeat(1001);


    private static final By OPEN_MODAL_BUTTON  = By.cssSelector("[data-testid='add-user-button']");
    private static final By MODAL = By.cssSelector("[role='dialog'][aria-label='Create user']");
    private static final By MODAL_CLOSE = By.cssSelector("[role='dialog'][aria-label='Create user'] button[aria-label='Close']");
    private static final By NAME_INPUT = By.cssSelector("[data-testid='user-name']");
    private static final By SURNAME_INPUT = By.cssSelector("[data-testid='user-surname']");
    private static final By USERNAME_INPUT = By.cssSelector("[data-testid='user-username']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-testid='user-password']");
    private static final By EMAIL_INPUT = By.cssSelector("[data-testid='user-email']");
    private static final By MANAGER_EMAIL_INPUT = By.cssSelector("[data-testid='user-manager-email']");
    private static final By NOTES_INPUT = By.cssSelector("[data-testid='user-note']");
    private static final By ADD_USER_BUTTON = By.cssSelector("[data-testid='create-user-button']");


    private void navigateToUsers() {
        driver.get(USERS_URL);
        wait.until(ExpectedConditions.elementToBeClickable(OPEN_MODAL_BUTTON));
    }


    private void assertStaysOnUsers() {
        wait.until(ExpectedConditions.urlContains("/users"));
        String currentUrl = driver.getCurrentUrl();
        assertNotNull(currentUrl);
        assertTrue(currentUrl.contains("/users"));
    }

    private void openUserCreateModal() {
        login();
        navigateToUsers();

        driver.findElement(OPEN_MODAL_BUTTON).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL));
    }

    @Test
    void userCreateModalClosesOnCloseButton() {
        openUserCreateModal();

        driver.findElement(MODAL_CLOSE).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL));

        assertTrue(driver.findElements(MODAL).isEmpty());
    }

    //Create user with valid data

    @Test
    void userCreateModalSavesValidUser() {
       openUserCreateModal();
       driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
       driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
       driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
       driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
       driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
       driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
       driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
       driver.findElement(ADD_USER_BUTTON).click();
       assertEquals(USERS_URL, driver.getCurrentUrl());
    }

    //Create  user with empty fields

    @Test
    void userCreateModalWithEmptyUsername() {
        openUserCreateModal();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWithEmptyName() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWithEmptySurname() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWithEmptyEmail() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWithEmptyPassword() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWithEmptyManagerEmail() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWithEmptyNotes() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(ADD_USER_BUTTON).click();
        assertEquals(USERS_URL, driver.getCurrentUrl());
    }

    @Test
    void userCreateModalShowsWithAllFieldsEmpty() {
        openUserCreateModal();
        driver.findElement(ADD_USER_BUTTON).click();
        assertEquals(USERS_URL, driver.getCurrentUrl());
    }

    // Create user with invalid data

    @Test
    void userCreateModalWitInvalidUsername() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(INVALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitInvalidName() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(INVALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitInvalidSurname() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(INVALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitInvalidEmail() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(INVALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitInvalidManagerEmail() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(INVALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    // Create user with long fields


    @Test
    void userCreateModalWitLongUsername() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(LONG_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitLongName() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(LONG_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitLongSurname() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(LONG_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitLongEmail() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(LONG_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitLongPassword() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(LONG_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitLongManagerEmail() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(LONG_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitLongNotes() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(LONG_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    // Create user with short fields


    @Test
    void userCreateModalWitShortUsername() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(SHORT_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitShortName() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(SHORT_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitShortSurname() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(SHORT_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(VALID_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }

    @Test
    void userCreateModalWitShortPassword() {
        openUserCreateModal();
        driver.findElement(USERNAME_INPUT).sendKeys(VALID_USERNAME);
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SURNAME_INPUT).sendKeys(VALID_SURNAME);
        driver.findElement(EMAIL_INPUT).sendKeys(VALID_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(SHORT_PASSWORD);
        driver.findElement(MANAGER_EMAIL_INPUT).sendKeys(VALID_MANAGER_EMAIL);
        driver.findElement(NOTES_INPUT).sendKeys(VALID_NOTES);
        driver.findElement(ADD_USER_BUTTON).click();
        assertStaysOnUsers();
    }


}
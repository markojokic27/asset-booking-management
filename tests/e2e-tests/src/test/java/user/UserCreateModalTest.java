package user;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserCreateModalTest extends BaseLogin {

    @BeforeMethod
    void setupUserPage() {
        login();

        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.USERS_URL);

        userPage.userOpenModal();
    }

    @Test
    void userCreateModalClosesOnCloseButton() {
        userPage.userModalClose();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));

    }

    //Create user with valid data

    @Test
    void userCreateModalSavesValidUser() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    //Create  user with empty fields

    @Test
    void userCreateModalWithEmptyName() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                "",
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWithEmptySurname() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                "",
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWithEmptyUsername() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                "",
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWithEmptyEmail() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                "",
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWithEmptyPassword() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                "",
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWithEmptyManagerEmail() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                "",
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWithEmptyNotes() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalShowsWithAllFieldsEmpty() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    // Create user with invalid data

    @Test
    void userCreateModalWitInvalidUsername() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.INVALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitInvalidName() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.INVALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitInvalidSurname() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.INVALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitInvalidEmail() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.INVALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitInvalidManagerEmail() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.INVALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    // Create user with long fields


    @Test
    void userCreateModalWitLongUsername() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.LONG_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitLongName() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.LONG_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitLongSurname() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.LONG_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitLongEmail() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.LONG_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitLongPassword() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.LONG_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitLongManagerEmail() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.LONG_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitLongNotes() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.LONG_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    // Create user with short fields


    @Test
    void userCreateModalWitShortUsername() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.SHORT_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitShortName() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.SHORT_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitShortSurname() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.SHORT_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userCreateModalWitShortPassword() {
        userPage.user(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.SHORT_PASSWORD,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }
}
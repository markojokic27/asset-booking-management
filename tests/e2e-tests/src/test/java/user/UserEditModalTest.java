package user;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class UserEditModalTest extends BaseLogin {

    @BeforeMethod
    void setupUserEditPage() {
        login();

        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.USERS_URL);
        userPage.userOpenEditModalByUsername(CommonConstants.EDIT_USER_USERNAME);
    }

    @Test
    void userEditModalClosesOnCloseButton() {
        userPage.userEditCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    // Edit user with empty fields

    @Test
    void userEditModalWithEmptyName() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                "",
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithEmptySurname() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                "",
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithEmptyEmail() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                "",
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithEmptyManagerEmail() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                "",
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithEmptyNotes() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithAllFieldsEmpty() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                "",
                "",
                "",
                "",
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    // Edit user with invalid data

    @Test
    void userEditModalWithInvalidName() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.INVALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithInvalidSurname() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.INVALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithInvalidEmail() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.INVALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithInvalidManagerEmail() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.INVALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    // Edit user with long fields

    @Test
    void userEditModalWithLongName() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.LONG_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithLongSurname() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.LONG_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithLongEmail() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.LONG_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithLongManagerEmail() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.LONG_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithLongNotes() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.LONG_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    // Edit user with short fields

    @Test
    void userEditModalWithShortName() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.SHORT_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }

    @Test
    void userEditModalWithShortSurname() {
        userPage.editUser(
                CommonConstants.VALID_ROLE,
                CommonConstants.VALID_STATUS,
                CommonConstants.VALID_NAME,
                CommonConstants.SHORT_SURNAME,
                CommonConstants.VALID_EMAIL,
                CommonConstants.VALID_MANAGER_EMAIL,
                CommonConstants.VALID_NOTES
        );
        assertTrue(waitForUrlContains(CommonConstants.USERS_URL));
    }
}
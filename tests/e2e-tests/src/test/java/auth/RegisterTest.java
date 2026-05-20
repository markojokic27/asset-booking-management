package auth;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class RegisterTest extends BaseLogin {

    @BeforeMethod
    @Override
    public void setUpBeforeEachTest(){
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.REGISTER_URL_EXTENSION);
    }

    // Register with valid data
    @Test
    void userCanRegister() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.LOGIN_URL_EXTENSION));

    }

    // Register with empty fields

    @Test
    void registerWithEmptyName() {
        registerPage.register(
                "" ,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithEmptySurname() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                "",
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithEmptyUsername() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                "",
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithEmptyPassword() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithAllFieldsEmpty() {
        registerPage.register(
                "",
                "",
                "",
                ""
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    // Register with invalid data

    @Test
    void registerWithInvalidName() {
        registerPage.register(
                CommonConstants.INVALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithInvalidSurname() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.INVALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithInvalidUsername() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.INVALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    // Register with short fields

    @Test
    void registerWithShortName() {
        registerPage.register(
                CommonConstants.SHORT_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithShortSurname() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.SHORT_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithShortUsername() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.SHORT_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithShortPassword() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.SHORT_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }


    // Register with long fields

    @Test
    void registerWithLongName() {
        registerPage.register(
                CommonConstants.LONG_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithLongSurname() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.LONG_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithLongUsername() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.LONG_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }

    @Test
    void registerWithLongPassword() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.LONG_PASSWORD
        );
        assertTrue(waitForUrlContains(CommonConstants.REGISTER_URL_EXTENSION));
    }
}

package playwright;

import config.ConfigFromFile;
import constants.CommonConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import playwright.base.BaseTest;

public class TestRegister extends BaseTest {

    @BeforeEach
    void goToRegisterPage() {
        page.navigate(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.REGISTER_URL_EXTENSION);
    }

    @Test
    void userCanRegister() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD
        );
        registerPage.assertOnLoginPage();
    }

    @Test
    void registerWithEmptyName() {
        registerPage.register(
                "",
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD);
        registerPage.assertOnRegisterPage();
    }

    @Test
    void registerWithInvalidName() {
        registerPage.register(
                CommonConstants.INVALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD);
        registerPage.assertOnRegisterPage();
    }

    @Test
    void registerWithInvalidUsername() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.INVALID_USERNAME,
                CommonConstants.VALID_PASSWORD);
        registerPage.assertOnRegisterPage();
    }

    @Test
    void registerWithShortPassword() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.SHORT_PASSWORD);
        registerPage.assertOnRegisterPage();
    }

    @Test
    void registerWithLongPassword() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                CommonConstants.LONG_PASSWORD);
        registerPage.assertOnRegisterPage();
    }

    @Test
    void registerWithEmptySurname() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                "",
                CommonConstants.VALID_USERNAME,
                CommonConstants.VALID_PASSWORD);
        registerPage.assertOnRegisterPage();
    }

    @Test
    void registerWithEmptyUsername() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                "",
                CommonConstants.VALID_PASSWORD);
        registerPage.assertOnRegisterPage();
    }

    @Test
    void registerWithEmptyPassword() {
        registerPage.register(
                CommonConstants.VALID_NAME,
                CommonConstants.VALID_SURNAME,
                CommonConstants.VALID_USERNAME,
                "");
        registerPage.assertOnRegisterPage();
    }
}
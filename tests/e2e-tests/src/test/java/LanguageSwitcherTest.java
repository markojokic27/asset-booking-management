import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class LanguageSwitcherTest extends BaseLogin {
    @BeforeMethod
    void setUp() {
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.BOOKINGS_URL_EXTENSION));

        languagePage.openLanguageMenu();

    }

    @Test
    void languageSwitcherSelectsEnglish() {
        languagePage.selectLanguage(languagePage.languageOptionEn);
        assertTrue(isElementVisible(languagePage.languageSwitcherButton));
    }

    @Test
    void languageSwitcherSelectsCroatia() {
        languagePage.selectLanguage(languagePage.languageOptionHr);
        assertTrue(isElementVisible(languagePage.languageSwitcherButton));
    }

    @Test
    void languageSwitcherSelectsDeutsch(){
        languagePage.selectLanguage(languagePage.languageOptionDe);
        assertTrue(isElementVisible(languagePage.languageSwitcherButton));
    }
}
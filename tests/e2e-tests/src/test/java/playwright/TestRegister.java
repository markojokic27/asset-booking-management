package playwright;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestRegister {

    static Playwright playwright;
    static Browser browser;
    Page page;

    static final String LOGIN_URL = "http://localhost:5173/login";
    static final String REGISTER_URL = "http://localhost:5173/register";


    static final String VALID_NAME = "Ivan";
    static final String VALID_SURNAME = "Ivic";
    static final String VALID_USERNAME = "ivanivic";
    static final String VALID_PASSWORD = "password.123";

    static final String INVALID_NAME = "Ivan!";
    static final String INVALID_SURNAME = "Ivic!";
    static final String INVALID_USERNAME = "ivanivic?";

    static final String SHORT_NAME = "i";
    static final String LONG_NAME = "i".repeat(101);
    static final String SHORT_SURNAME = "i";
    static final String LONG_SURNAME = "i".repeat(101);
    static final String SHORT_USERNAME = "i";
    static final String LONG_USERNAME = "i".repeat(51);
    static final String SHORT_PASSWORD = "pas123";
    static final String LONG_PASSWORD = "p".repeat(51);

    static final String NAME_INPUT = "[data-testid='name']";
    static final String SURNAME_INPUT = "[data-testid='surname']";
    static final String USERNAME_INPUT = "[data-testid='username']";
    static final String PASSWORD_INPUT = "[data-testid='password']";
    static final String REGISTER_BUTTON = "[data-testid='register-button']";

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();

        boolean headless = !Boolean.parseBoolean(System.getenv().getOrDefault("HEADED", "true"));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(100));
    }

    @BeforeEach
    void setup() {
        page = browser.newPage();
        page.navigate(REGISTER_URL);
        assertThat(page.locator(NAME_INPUT)).isVisible();
    }

    @AfterEach
    void closePage() {
        page.close();
    }


    @Test
    void userCanRegister() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();

        assertThat(page).hasURL(LOGIN_URL);
    }


    @Test
    void registerWithEmptyName() {
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithEmptySurname() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithEmptyUsername() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithEmptyPassword() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithAllFieldsEmpty() {
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }


    @Test
    void registerWithInvalidName() {
        page.locator(NAME_INPUT).fill(INVALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithInvalidSurname() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(INVALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithInvalidUsername() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(INVALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }


    @Test
    void registerWithShortName() {
        page.locator(NAME_INPUT).fill(SHORT_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithShortPassword() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(SHORT_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithShortUsername() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(SHORT_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithShortSurname() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(SHORT_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }


    @Test
    void registerWithLongName() {
        page.locator(NAME_INPUT).fill(LONG_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithLongUsername() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(LONG_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithLongPassword() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(VALID_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(LONG_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }

    @Test
    void registerWithLongSurname() {
        page.locator(NAME_INPUT).fill(VALID_NAME);
        page.locator(SURNAME_INPUT).fill(LONG_SURNAME);
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(REGISTER_BUTTON).click();
        assertThat(page).hasURL(REGISTER_URL);
    }
}
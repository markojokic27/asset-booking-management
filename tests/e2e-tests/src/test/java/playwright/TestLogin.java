package playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestLogin {

    static Playwright playwright;
    static Browser browser;
    Page page;

    static final String LOGIN_URL = "http://localhost:5173/login";
    static final String BOOKINGS_URL = "http://localhost:5173/bookings";

    static final String VALID_USERNAME = "user_admin";
    static final String VALID_PASSWORD = "admin123";
    static final String INVALID_USERNAME = "user_admin!";
    static final String INVALID_PASSWORD = "pass.1234";

    static final String USERNAME_INPUT = "[data-testid='username']";
    static final String PASSWORD_INPUT = "[data-testid='password']";
    static final String LOGIN_BUTTON   = "[data-testid='login-button']";

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();

        boolean headless = !Boolean.parseBoolean(
                System.getenv().getOrDefault("HEADED", "true"));

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(headless));
    }

    @BeforeEach
    void setup() {
        page = browser.newPage();
        page.navigate(LOGIN_URL);
        assertThat(page.locator(USERNAME_INPUT)).isVisible();
    }

    @AfterEach
    void closePage() {
        page.close();
    }

    @Test
    void userCanLogin() {
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(LOGIN_BUTTON).click();

        assertThat(page).hasURL(BOOKINGS_URL);
    }

    @Test
    void loginWithEmptyUsername() {
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(LOGIN_BUTTON).click();

        assertThat(page).hasURL(LOGIN_URL);
    }

    @Test
    void loginWithEmptyPassword() {
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(LOGIN_BUTTON).click();

        assertThat(page).hasURL(LOGIN_URL);
    }

    @Test
    void loginWithBothFieldsEmpty() {
        page.locator(LOGIN_BUTTON).click();

        assertThat(page).hasURL(LOGIN_URL);
    }

    @Test
    void loginWithIncorrectUsername() {
        page.locator(USERNAME_INPUT).fill(INVALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(VALID_PASSWORD);
        page.locator(LOGIN_BUTTON).click();

        assertThat(page).hasURL(LOGIN_URL);
    }

    @Test
    void loginWithIncorrectPassword() {
        page.locator(USERNAME_INPUT).fill(VALID_USERNAME);
        page.locator(PASSWORD_INPUT).fill(INVALID_PASSWORD);
        page.locator(LOGIN_BUTTON).click();

        assertThat(page).hasURL(LOGIN_URL);
    }
}
package playwright;

import com.microsoft.playwright.BrowserType;
import org.junit.jupiter.api.*;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import java.util.regex.Pattern;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTestP {

    static Playwright playwright;
    static Browser browser;
    Page page;

    static String BASE_URL = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:5173");

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();

        // headless false is needed for CI
        boolean headless = !Boolean.parseBoolean(
                System.getenv().getOrDefault("HEADED", "false"));

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(headless));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createPage() {
        page = browser.newPage();
    }

    @AfterEach
    void closePage() {
        page.close();
    }

    @Test
    void userCanLogin() {
        // Fix: use BASE_URL instead of hardcoded localhost for CI
        page.navigate(BASE_URL + "/login");
        page.locator("[data-testid='username']").fill("user_admin");
        page.locator("[data-testid='password']").fill("admin123");
        page.locator("[data-testid='login-button']").click();
        page.waitForURL("http://localhost:5173/assets");
        assertThat(page).hasURL("http://localhost:5173/assets");
    }

    @Test
    void loginWithEmptyUsername() {
        page.navigate(BASE_URL + "/login");
        page.locator("[data-testid='password']").fill("admin123");
        page.locator("[data-testid='login-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/login.*"));
    }

    @Test
    void loginWithEmptyPassword() {
        page.navigate("http://localhost:5173/login");
        page.locator("[data-testid='username']").fill("user_admin");
        page.locator("[data-testid='login-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/login.*"));
    }

    @Test
    void loginWithIncorrectUsername() {
        page.navigate("http://localhost:5173/login");
        page.locator("[data-testid='username']").fill("user_admin!");
        page.locator("[data-testid='password']").fill("admin123");
        page.locator("[data-testid='login-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/login.*"));
    }

    @Test
    void loginWithIncorrectPassword() {
        page.navigate("http://localhost:5173/login");
        page.locator("[data-testid='username']").fill("user_admin");
        page.locator("[data-testid='password']").fill("passw2");
        page.locator("[data-testid='login-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/login.*"));
    }

    @Test
    void loginWithBothFieldsEmpty() {
        page.navigate("http://localhost:5173/login");
        page.locator("[data-testid='login-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/login.*"));
    }
}

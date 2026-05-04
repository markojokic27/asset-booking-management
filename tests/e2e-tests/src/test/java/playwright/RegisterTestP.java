package playwright;

import com.microsoft.playwright.BrowserType;
import org.junit.jupiter.api.*;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import java.util.regex.Pattern;
import com.microsoft.playwright.Playwright;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterTestP {

    static Playwright playwright;
    static Browser browser;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();

        boolean headless = !Boolean.parseBoolean(
                System.getenv().getOrDefault("HEADED", "true"));

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
    void userCanRegister() {
        page.navigate("http://localhost:5173/register");
        page.locator("[name='name']").waitFor();
        page.locator("[name='name']").fill("Ivan");
        page.locator("[data-testid='surname']").fill("Ivic");
        page.locator("[name='username']").fill("ivanivic2");
        page.locator("[data-testid='password']").fill("password.123");
        page.locator("[data-testid='register-button']").click();
        page.waitForURL("**/login**");
        assertThat(page).hasURL(Pattern.compile(".*/login.*"));
    }

    @Test
    void registerWithEmptyName() {
        page.navigate("http://localhost:5173/register");
        page.locator("[name='name']").waitFor();
        page.locator("[data-testid='surname']").fill("Ivic");
        page.locator("[name='username']").fill("ivanivic2");
        page.locator("[data-testid='password']").fill("password.123");
        page.locator("[data-testid='register-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/register.*"));
    }

    @Test
    void registerWithEmptySurname() {
        page.navigate("http://localhost:5173/register");
        page.locator("[name='name']").waitFor();
        page.locator("[name='name']").fill("Ivan");
        page.locator("[name='username']").fill("ivanivic2");
        page.locator("[data-testid='password']").fill("password.123");
        page.locator("[data-testid='register-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/register.*"));
    }

    @Test
    void registerWithEmptyUsername() {
        page.navigate("http://localhost:5173/register");
        page.locator("[name='name']").waitFor();
        page.locator("[name='name']").fill("Ivan");
        page.locator("[data-testid='surname']").fill("Ivic");
        page.locator("[data-testid='password']").fill("password.123");
        page.locator("[data-testid='register-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/register.*"));
    }

    @Test
    void registerWithEmptyPassword() {
        page.navigate("http://localhost:5173/register");
        page.locator("[name='name']").waitFor();
        page.locator("[name='name']").fill("Ivan");
        page.locator("[data-testid='surname']").fill("Ivic");
        page.locator("[name='username']").fill("ivanivic2");
        page.locator("[data-testid='register-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/register.*"));
    }

    @Test
    void registerWithAllFieldsEmpty() {
        page.navigate("http://localhost:5173/register");
        page.locator("[name='name']").waitFor();
        page.locator("[data-testid='register-button']").click();
        assertThat(page).hasURL(Pattern.compile(".*/register.*"));
    }
}
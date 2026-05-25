package playwright.base;

import com.microsoft.playwright.*;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.junit.jupiter.api.*;
import playwright.factory.PageAndHandlerFactory;
public class BaseTest extends PageAndHandlerFactory {

    static Playwright playwright;
    static Browser browser;

    public BaseTest() {
        super(null);
    }
    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        boolean headless = Boolean.parseBoolean(
                System.getenv().getOrDefault("HEADLESS", "false"));
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(headless));
    }
    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void setUp() {
        page = browser.newPage();
        page.navigate(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.LOGIN_URL_EXTENSION);
        setupPagesAndHandlers();
    }

    @AfterEach
    void tearDown() {
        page.close();
    }

    protected void login() {
            loginPage.typeUsername(CommonConstants.ADMIN_USERNAME);
            loginPage.typePassword(CommonConstants.ADMIN_PASS);
            loginPage.clickLoginButton();
            loginPage.assertOnBookingsPage();
    }
}
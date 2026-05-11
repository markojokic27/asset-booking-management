import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseTest {

    private static final String DEFAULT_BROWSER  = "chrome";
    private static final String DEFAULT_SLEEP_MS = "5000";
    private static final int    WAIT_TIMEOUT_SEC = 10;

    protected static final String LOGIN_URL      = "http://localhost:5173/login";
    protected static final String POST_LOGIN_URL = "http://localhost:5173/bookings";
    protected static final String LOGIN_USERNAME = "user_admin";
    protected static final String LOGIN_PASSWORD = "admin123";

    private static final By USERNAME_INPUT = By.cssSelector("[data-testid='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-testid='password']");
    private static final By LOGIN_BUTTON   = By.cssSelector("[data-testid='login-button']");

    protected WebDriver     driver;
    protected WebDriverWait wait;

    static {
        Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder")
                .setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.silentOutput", "true");
    }

    @BeforeEach
    void setUp() {
        String browser = System.getProperty("browser", DEFAULT_BROWSER);
        driver = BrowserFactory.create(browser);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SEC));
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        int sleep = Integer.parseInt(System.getProperty("sleepTime", DEFAULT_SLEEP_MS));
        Thread.sleep(sleep);
        if (driver != null) driver.quit();
    }

    protected void login() {
        driver.get(LOGIN_URL);
        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(USERNAME_INPUT)
        );
        usernameInput.sendKeys(LOGIN_USERNAME);
        driver.findElement(PASSWORD_INPUT).sendKeys(LOGIN_PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();
        wait.until(ExpectedConditions.urlToBe(POST_LOGIN_URL));
    }
}
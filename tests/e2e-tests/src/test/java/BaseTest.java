import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    static {
        Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder").setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.silentOutput", "true");
    }

    @BeforeEach
    void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                options.setBinary("/snap/firefox/current/usr/lib/firefox/firefox");
                driver = new FirefoxDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Nepodržan browser: " + browser);
        }

        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        int sleep = Integer.parseInt(System.getProperty("sleepTime", "5000"));
        Thread.sleep(sleep);

        if (driver != null) {
            driver.quit();
        }
    }

    protected void login() {
        driver.get("http://localhost:5173/login");
        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='username']"))
        );
        usernameInput.sendKeys("user_admin");
        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("admin123");
        driver.findElement(By.cssSelector("[data-testid='login-button']")).click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/bookings"));
    }
}
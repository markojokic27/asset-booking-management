import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

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

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    @AfterEach
    void tearDown() throws InterruptedException {
        int sleep = Integer.parseInt(System.getProperty("sleepTime", "5000"));
        Thread.sleep(sleep);

        if(driver != null){
            driver.quit();    
        }
        
    }

    @Test
    void userCanLogin() {
        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='username']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        driver.findElement(By.cssSelector("[name='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/"));
        assertEquals("http://localhost:5173/", driver.getCurrentUrl());
    }

    @Test
    void LoginWithEmptyUsername() {
        driver.get("http://localhost:5173/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[name='username']")));

        driver.findElement(By.cssSelector("[name='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void LoginWithEmptyPassword() {
        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='username']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void LoginWithIncorrectUsername() {
        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='username']")
                )
        );
        usernameInput.sendKeys("ivanivic!");

        driver.findElement(By.cssSelector("[name='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void LoginWithIncorrectPassword() {
        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='username']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        driver.findElement(By.cssSelector("[name='password']")).sendKeys("passw2");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void LoginWithBothFieldsEmpty() {
        driver.get("http://localhost:5173/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[name='username']")));

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
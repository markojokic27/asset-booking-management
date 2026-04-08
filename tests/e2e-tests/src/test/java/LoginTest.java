import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
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
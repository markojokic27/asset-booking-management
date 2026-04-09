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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterTest {

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
    void userCanRegister() {
        driver.get("http://localhost:5173/register");

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='name']")
                )
        );
        nameInput.sendKeys("Ivan");

        driver.findElement(By.cssSelector("[name='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[name='username']")).sendKeys("ivanivic2");
        driver.findElement(By.cssSelector("[name='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

       // wait.until(ExpectedConditions.urlContains("/login"));
       // assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void RegisterWithEmptyName() {
        driver.get("http://localhost:5173/register");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[name='name']")));

        driver.findElement(By.cssSelector("[name='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[name='username']")).sendKeys("ivanivic2");
        driver.findElement(By.cssSelector("[name='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }

    @Test
    void RegisterWithEmptySurname() {
        driver.get("http://localhost:5173/register");

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='name']")
                )
        );
        nameInput.sendKeys("Ivan");

        driver.findElement(By.cssSelector("[name='username']")).sendKeys("ivanivic2");
        driver.findElement(By.cssSelector("[name='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }

    @Test
    void RegisterWithEmptyUsername() {
        driver.get("http://localhost:5173/register");

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='name']")
                )
        );
        nameInput.sendKeys("Ivan");

        driver.findElement(By.cssSelector("[name='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[name='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }

    @Test
    void RegisterWithEmptyPassword() {
        driver.get("http://localhost:5173/register");

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='name']")
                )
        );
        nameInput.sendKeys("Ivan");

        driver.findElement(By.cssSelector("[name='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[name='username']")).sendKeys("ivanivic2");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }

    @Test
    void RegisterWithAllFieldsEmpty() {
        driver.get("http://localhost:5173/register");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[name='name']")));

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }
}
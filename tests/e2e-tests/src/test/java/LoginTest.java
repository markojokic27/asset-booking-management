import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    @Test
    void userCanLogin() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username-input']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        WebElement passwordInput = driver.findElement(
                By.cssSelector("[data-testid='password-input']")
        );
        passwordInput.sendKeys("password.123");

        WebElement loginButton = driver.findElement(
                By.cssSelector("[data-testid='login-button']")
        );
        loginButton.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/"));
        assertEquals("http://localhost:5173/", driver.getCurrentUrl());

        Thread.sleep(5000);
        
        driver.quit();
    }

    
    @Test
    void LoginWithEmptyUsername() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username-input']")
                )
        );
        usernameInput.sendKeys("");

        WebElement passwordInput = driver.findElement(
                By.cssSelector("[data-testid='password-input']")
        );
        passwordInput.sendKeys("password.123");

        WebElement loginButton = driver.findElement(
                By.cssSelector("[data-testid='login-button']")
        );
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));

        Thread.sleep(5000);

        driver.quit();
    }

    @Test
    void LoginWithEmptyPassword() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username-input']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        WebElement passwordInput = driver.findElement(
                By.cssSelector("[data-testid='password-input']")
        );
        passwordInput.sendKeys("");

        WebElement loginButton = driver.findElement(
                By.cssSelector("[data-testid='login-button']")
        );
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));

        Thread.sleep(5000);


        driver.quit();
    }

    @Test
    void LoginWithIncorrectUsername() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username-input']")
                )
        );
        usernameInput.sendKeys("ivanivic!");

        WebElement passwordInput = driver.findElement(
                By.cssSelector("[data-testid='password-input']")
        );
        passwordInput.sendKeys("password.123");

        WebElement loginButton = driver.findElement(
                By.cssSelector("[data-testid='login-button']")
        );
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));

        Thread.sleep(5000);

        driver.quit();
    }


    @Test
    void LoginWithIncorrectPassword() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username-input']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        WebElement passwordInput = driver.findElement(
                By.cssSelector("[data-testid='password-input']")
        );
        passwordInput.sendKeys("passw2");

        WebElement loginButton = driver.findElement(
                By.cssSelector("[data-testid='login-button']")
        );
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));

        Thread.sleep(5000);

        driver.quit();
    }


    @Test
    void LoginWithBothFieldsEmpty() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username-input']")
                )
        );
        usernameInput.sendKeys("");

        WebElement passwordInput = driver.findElement(
                By.cssSelector("[data-testid='password-input']")
        );
        passwordInput.sendKeys("");

        WebElement loginButton = driver.findElement(
                By.cssSelector("[data-testid='login-button']")
        );
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));

        Thread.sleep(5000);

        driver.quit();
    }


 

    
}
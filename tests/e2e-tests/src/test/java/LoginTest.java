import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest extends BaseTest {


    @Test
    void userCanLogin() {
        login(); 
        assertEquals("http://localhost:5173/", driver.getCurrentUrl());
    }

    @Test
    void LoginWithEmptyUsername() {
        driver.get("http://localhost:5173/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='username']")));

        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='login-button']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    void LoginWithEmptyPassword() {
        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        driver.findElement(By.cssSelector("[data-testid='login-button']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    void LoginWithIncorrectUsername() {
        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username']")
                )
        );
        usernameInput.sendKeys("ivanivic!");

        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='login-button']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    void LoginWithIncorrectPassword() {
        driver.get("http://localhost:5173/login");

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='username']")
                )
        );
        usernameInput.sendKeys("ivanivic");

        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("passw2");
        driver.findElement(By.cssSelector("[data-testid='login-button']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    void LoginWithBothFieldsEmpty() {
        driver.get("http://localhost:5173/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='username']")));

        driver.findElement(By.cssSelector("[data-testid='login-button']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
    }
}
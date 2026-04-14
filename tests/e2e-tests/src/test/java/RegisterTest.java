import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterTest extends BaseTest{


    @Test
    void userCanRegister() {
        driver.get("http://localhost:5173/register");

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[name='name']")
                )
        );
        nameInput.sendKeys("Ivan");

        driver.findElement(By.cssSelector("[data-testid='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[name='username']")).sendKeys("ivanivic2");
        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='register-button']")).click();

       wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    void RegisterWithEmptyName() {
        driver.get("http://localhost:5173/register");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[name='name']")));

        driver.findElement(By.cssSelector("[data-testid='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[name='username']")).sendKeys("ivanivic2");
        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='register-button']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
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
        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='register-button']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
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

        driver.findElement(By.cssSelector("[data-testid='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[data-testid='password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='register-button']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
    }

    @Test
    void RegisterWithEmptyPassword() {
        driver.get("http://localhost:5173/register");

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='name']")
                )
        );
        nameInput.sendKeys("Ivan");

        driver.findElement(By.cssSelector("[data-testid='surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[name='username']")).sendKeys("ivanivic2");
        driver.findElement(By.cssSelector("[data-testid='register-button']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
    }

    @Test
    void RegisterWithAllFieldsEmpty() {
        driver.get("http://localhost:5173/register");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='name']")));

        driver.findElement(By.cssSelector("[data-testid='register-button']")).click();

        wait.until(ExpectedConditions.urlContains("/register"));
    }
}
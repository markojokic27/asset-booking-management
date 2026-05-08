import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AddUserTest extends BaseTest {

    private void navigateToUsers() {
        WebElement usersLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/users']")
                )
        );
        usersLink.click();
        wait.until(ExpectedConditions.urlContains("/users"));
    }

    private void openUserCreateModal() {
        login();
        navigateToUsers();

        WebElement createButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'Novi')]")
                )
        );
        createButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Create user']")
        ));
    }

    private void clickCreateAndAssertModalStillOpen() {
        driver.findElement(By.cssSelector("[data-testid='create-user-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Create user']")
        ).isDisplayed());
    }

    @Test
    void userCreateModalClosesOnCloseButton() {
        openUserCreateModal();

        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Create user'] button[aria-label='Close']")
        ).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Create user']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Create user']")
        ).isEmpty());
    }

    @Test
    void userCreateModalSavesValidUser() {
        openUserCreateModal();

        driver.findElement(By.cssSelector("[data-testid='user-username']")).sendKeys("ivanivic");
        driver.findElement(By.cssSelector("[data-testid='user-name']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-testid='user-surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[data-testid='user-email']")).sendKeys("ivan.ivic@example.com");
        driver.findElement(By.cssSelector("[data-testid='user-password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).sendKeys("antea.ntic@example.com");

        driver.findElement(By.cssSelector("[data-testid='create-user-button']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Create user']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Create user']")
        ).isEmpty());
    }

    @Test
    void userCreateModalShowsErrorForEmptyUsername() {
        openUserCreateModal();
        driver.findElement(By.cssSelector("[data-testid='user-name']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-testid='user-surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[data-testid='user-email']")).sendKeys("ivan.ivic@example.com");
        driver.findElement(By.cssSelector("[data-testid='user-password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).sendKeys("antea.ntic@example.com");
        clickCreateAndAssertModalStillOpen();
    }

    @Test
    void userCreateModalShowsErrorForEmptyFirstName() {
        openUserCreateModal();
        driver.findElement(By.cssSelector("[data-testid='user-username']")).sendKeys("ivanivic");
        driver.findElement(By.cssSelector("[data-testid='user-surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[data-testid='user-email']")).sendKeys("ivan.ivic@example.com");
        driver.findElement(By.cssSelector("[data-testid='user-password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).sendKeys("antea.ntic@example.com");
        clickCreateAndAssertModalStillOpen();
    }

    @Test
    void userCreateModalShowsErrorForEmptyLastName() {
        openUserCreateModal();
        driver.findElement(By.cssSelector("[data-testid='user-username']")).sendKeys("ivanivic");
        driver.findElement(By.cssSelector("[data-testid='user-name']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-testid='user-email']")).sendKeys("ivan.ivic@example.com");
        driver.findElement(By.cssSelector("[data-testid='user-password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).sendKeys("antea.ntic@example.com");
        clickCreateAndAssertModalStillOpen();
    }

    @Test
    void userCreateModalShowsErrorForEmptyEmail() {
        openUserCreateModal();
        driver.findElement(By.cssSelector("[data-testid='user-username']")).sendKeys("ivanivic");
        driver.findElement(By.cssSelector("[data-testid='user-name']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-testid='user-surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[data-testid='user-password']")).sendKeys("password.123");
        driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).sendKeys("antea.ntic@example.com");
        clickCreateAndAssertModalStillOpen();
    }

    @Test
    void userCreateModalShowsErrorForEmptyPassword() {
        openUserCreateModal();
        driver.findElement(By.cssSelector("[data-testid='user-username']")).sendKeys("ivanivic");
        driver.findElement(By.cssSelector("[data-testid='user-name']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-testid='user-surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[data-testid='user-email']")).sendKeys("ivan.ivic@example.com");
        driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).sendKeys("antea.ntic@example.com");
        clickCreateAndAssertModalStillOpen();
    }

    @Test
    void userCreateModalShowsErrorForEmptyManagerEmail() {
        openUserCreateModal();
        driver.findElement(By.cssSelector("[data-testid='user-username']")).sendKeys("ivanivic");
        driver.findElement(By.cssSelector("[data-testid='user-name']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-testid='user-surname']")).sendKeys("Ivic");
        driver.findElement(By.cssSelector("[data-testid='user-email']")).sendKeys("ivan.ivic@example.com");
        driver.findElement(By.cssSelector("[data-testid='user-password']")).sendKeys("password.123");
        clickCreateAndAssertModalStillOpen();
    }

    @Test
    void userCreateModalShowsErrorForAllFieldsEmpty() {
        openUserCreateModal();

        driver.findElement(By.cssSelector("[data-testid='create-user-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Create user']")
        ).isDisplayed());
    }
}
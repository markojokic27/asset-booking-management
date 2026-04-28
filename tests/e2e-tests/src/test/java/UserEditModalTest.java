import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class UserEditModalTest extends BaseTest {

    private void navigateToUsers() {
        WebElement usersLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/users']")
                )
        );
        usersLink.click();
        wait.until(ExpectedConditions.urlContains("/users"));
    }

    private void openUserEditModal() {
        login();
        navigateToUsers();

        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[aria-label='Edit user']")
                )
        );
        editButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ));
    }

    @Test
    void userEditModalDisplaysFormFields() {
        openUserEditModal();

        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-username']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-name']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-surname']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-email']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-role']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-status']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-department-id']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-note']")).isDisplayed());
    }

    @Test
    void userEditModalClosesOnCloseButton() {
        openUserEditModal();

        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user'] button[aria-label='Close']")
        ).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isEmpty());
    }

    @Test
    void userEditModalSavesValidChanges() {
        openUserEditModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='user-name']"));
        nameInput.clear();
        nameInput.sendKeys("Ane");

        driver.findElement(By.cssSelector("[data-testid='button-save']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isEmpty());
    }

    @Test
    void userEditModalShowsErrorForEmptyFirstName() {
        openUserEditModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='user-name']"));
        nameInput.clear();

        driver.findElement(By.cssSelector("[data-testid='button-save']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isDisplayed());
    }

    @Test
    void userEditModalShowsErrorForEmptyLastName() {
        openUserEditModal();

        WebElement surnameInput = driver.findElement(By.cssSelector("[data-testid='user-surname']"));
        surnameInput.clear();

        driver.findElement(By.cssSelector("[data-testid='button-save']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isDisplayed());
    }

    @Test
    void userEditModalShowsErrorForEmptyEmail() {
        openUserEditModal();

        WebElement emailInput = driver.findElement(By.cssSelector("[data-testid='user-email']"));
        emailInput.clear();

        driver.findElement(By.cssSelector("[data-testid='button-save']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isDisplayed());
    }
}
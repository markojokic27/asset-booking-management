import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class UserViewModalTest extends BaseTest {

    private void navigateToUsers() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/users']")
        )).click();
        wait.until(ExpectedConditions.urlContains("/users"));
    }

    private void openUserViewModal() {
        login();
        navigateToUsers();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table tbody tr")
        ));

        WebElement viewButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("table tbody tr:first-child td:last-child button:first-child")
                )
        );
        viewButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog']")
        ));
    }

    @Test
    void userViewModalDisplaysCorrectData() {
        openUserViewModal();

        assertFalse(driver.findElement(By.cssSelector("[data-testid='user-name']")).getText().isBlank());
        assertFalse(driver.findElement(By.cssSelector("[data-testid='user-email']")).getText().isBlank());
        assertFalse(driver.findElement(By.cssSelector("[data-testid='user-username']")).getText().isBlank());
        assertFalse(driver.findElement(By.cssSelector("[data-testid='user-role']")).getText().isBlank());
        assertFalse(driver.findElement(By.cssSelector("[data-testid='user-department-id']")).getText().isBlank());
        assertFalse(driver.findElement(By.cssSelector("[data-testid='user-manager-email']")).getText().isBlank());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='user-note']")).isDisplayed());
    }

    @Test
    void userViewModalClosesOnCloseButton() {
        openUserViewModal();

        WebElement closeButton = driver.findElement(
                By.cssSelector("[role='dialog'] button")
        );
        closeButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog']")
        ).isEmpty());
    }
}
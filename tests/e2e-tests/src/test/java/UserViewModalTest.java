import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class UserViewModalTest extends BaseTest {

    private void navigateToUsers() {
        WebElement usersLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/users']")
                )
        );
        usersLink.click();
        wait.until(ExpectedConditions.urlContains("/users"));
    }

    private void openUserViewModal() {
        login();
        navigateToUsers();

        WebElement viewButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[aria-label='View user']")
                )
        );
        viewButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='User details']")
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
        assertFalse(driver.findElement(By.cssSelector("[data-testid='user-note']")).getText().isBlank());
    }

    @Test
    void userViewModalClosesOnCloseButton() {
        openUserViewModal();

        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='User details'] button[aria-label='Close']")
        ).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='User details']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='User details']")
        ).isEmpty());
    }
}
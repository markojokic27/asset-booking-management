import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class UserBookingModalTest extends BaseTest {

    private void navigateToUsers() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/users']")
        )).click();
        wait.until(ExpectedConditions.urlContains("/users"));
    }

    private void openUserBookingsModal() {
        login();
        navigateToUsers();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table tbody tr")
        ));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("table tbody tr:first-child [data-testid='user-bookings-button']")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog']")
        ));
    }

    private void assertModalClosed() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog']")
        ));
        assertTrue(driver.findElements(By.cssSelector("[role='dialog']")).isEmpty());
    }

    @Test
    void userBookingsModalOpensWithUserName() {
        openUserBookingsModal();

        assertFalse(driver.findElement(
                By.cssSelector("[role='dialog'] p")
        ).getText().isBlank());
    }

    @Test
    void userBookingsModalClosesOnCloseButton() {
        openUserBookingsModal();

        driver.findElement(
                By.cssSelector("[role='dialog'] button")
        ).click();

        assertModalClosed();
    }
}
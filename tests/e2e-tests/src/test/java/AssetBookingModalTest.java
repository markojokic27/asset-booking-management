import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AssetBookingModalTest extends BaseTest {

    private void navigateToAssets() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/assets']")
        )).click();
        wait.until(ExpectedConditions.urlContains("/assets"));
    }

    private void openBookingsModal() {
        login();
        navigateToAssets();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table tbody tr")
        ));

        WebElement bookingsButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//table//tbody//tr)[1]//button[contains(text(), 'Bookings')]")
                )
        );
        bookingsButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".fixed.inset-0")
        ));
    }

    private void assertModalOpen() {
        assertTrue(driver.findElement(By.cssSelector(".fixed.inset-0")).isDisplayed());
    }

    private void assertModalClosed() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".fixed.inset-0")
        ));
        assertTrue(driver.findElements(By.cssSelector(".fixed.inset-0")).isEmpty());
    }

    @Test
    void assetBookingsModalOpens() {
        openBookingsModal();

        assertTrue(driver.findElement(
                By.cssSelector("[data-testid='asset-bookings-modal']")
        ).isDisplayed());
    }

    @Test
    void assetBookingsModalClosesOnCloseButton() {
        openBookingsModal();

        driver.findElement(
                By.cssSelector("button[aria-label='Close bookings modal']")
        ).click();

        assertModalClosed();
    }

}
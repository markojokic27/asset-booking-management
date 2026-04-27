import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AssetViewModalTest extends BaseTest {

   private void navigateToAssets() {
    WebElement assetsLink = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.cssSelector("nav a[href='/assets']")
            )
    );

    assetsLink.click();

    wait.until(ExpectedConditions.urlContains("/assets"));
}

    private void openAssetViewModal() {
        login();
        navigateToAssets();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table tbody tr")
        ));

        WebElement viewButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("table tbody tr:first-child [aria-label='View asset']")
                )
        );

        viewButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ));
    }

    @Test
    void assetViewModalDisplaysData() {
        openAssetViewModal();

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='asset-name-value']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='asset-category-value']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='asset-description-value']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='asset-status-badge']")
        ).getText().isBlank());
    }

    @Test
    void assetViewModalClosesOnCloseButton() {
        openAssetViewModal();

        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Asset details'] button[aria-label='Close']")
        ).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isEmpty());
    }
}
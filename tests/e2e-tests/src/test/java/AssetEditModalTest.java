import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

public class AssetEditModalTest extends BaseTest {

    private void navigateToAssets() {
        WebElement assetsLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/assets']")
                )
        );

        assetsLink.click();
        wait.until(ExpectedConditions.urlContains("/assets"));
    }

    private void openAssetEditModal() {
        login();
        navigateToAssets();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table tbody tr")
        ));

        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("table tbody tr:first-child [data-testid='edit-asset-button']")
                )
        );

        editButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ));
    }

    @Test
    void assetEditModalDisplaysFormFields() {
        openAssetEditModal();

        assertTrue(driver.findElement(By.cssSelector("[data-testid='asset-status']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='asset-category']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='asset-name']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='asset-description']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='asset-location']")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector("[data-testid='save-asset-button']")).isDisplayed());
    }

    @Test
    void assetEditModalClosesOnCloseButton() {
        openAssetEditModal();

        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit asset'] button[aria-label='Close']")
        ).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isEmpty());
    }

        @Test
        void assetEditModalSavesValidChanges() {
            openAssetEditModal();

            WebElement categorySelect = driver.findElement(By.cssSelector("[data-testid='asset-category']"));
            categorySelect.click();

            wait.until(driver -> new Select(categorySelect).getOptions().size() > 1);

            Select categoryDropdown = new Select(categorySelect);
            categoryDropdown.selectByIndex(1);

            WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='asset-name']"));
            nameInput.clear();
            nameInput.sendKeys("Dell Latitude 5441");

            WebElement locationInput = driver.findElement(By.cssSelector("[data-testid='asset-location']"));
            locationInput.clear();
            locationInput.sendKeys("Room 101");

            driver.findElement(By.cssSelector("[data-testid='save-asset-button']")).click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("[role='dialog'][aria-label='Edit asset']")
            ));

            assertTrue(driver.findElements(
                    By.cssSelector("[role='dialog'][aria-label='Edit asset']")
            ).isEmpty());
        }

    @Test
    void assetEditModalShowsErrorForEmptyName() {
        openAssetEditModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='asset-name']"));
        nameInput.clear();

        driver.findElement(By.cssSelector("[data-testid='save-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isDisplayed());
    }

    @Test
    void assetEditModalShowsErrorForEmptyLocation() {
        openAssetEditModal();

        WebElement locationInput = driver.findElement(By.cssSelector("[data-testid='asset-location']"));
        locationInput.clear();

        driver.findElement(By.cssSelector("[data-testid='save-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isDisplayed());
    }

    @Test
    void assetEditModalCanChangeStatus() {
        openAssetEditModal();

        Select statusSelect = new Select(
                driver.findElement(By.cssSelector("[data-testid='asset-status']"))
        );

        statusSelect.selectByValue("INACTIVE");

        assertEquals(
                "INACTIVE",
                driver.findElement(By.cssSelector("[data-testid='asset-status']")).getAttribute("value")
        );
    }

    @Test
    void assetEditModalShowsErrorForEmptyCategory() {
        openAssetEditModal();

        Select categorySelect = new Select(
                driver.findElement(By.cssSelector("[data-testid='asset-category']"))
        );

        categorySelect.selectByValue("");

        driver.findElement(By.cssSelector("[data-testid='save-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isDisplayed());
    }
}
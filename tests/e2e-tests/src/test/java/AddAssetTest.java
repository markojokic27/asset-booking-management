import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

public class AddAssetTest extends BaseTest {

    private void navigateToAssets() {
        WebElement assetsLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/assets']")
                )
        );
        assetsLink.click();
        wait.until(ExpectedConditions.urlContains("/assets"));
    }

    private void openAssetAddModal() {
        login();
        navigateToAssets();

        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'New asset')]")
                )
        );
        addButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ));
    }

    private void fillValidAssetExcept(String fieldToSkip) {
        if (!fieldToSkip.equals("category")) {
            WebElement category = driver.findElement(By.cssSelector("[data-testid='asset-category']"));
            category.click();
            new Select(category).selectByIndex(1);
        }

        if (!fieldToSkip.equals("name")) {
            driver.findElement(By.cssSelector("[data-testid='asset-name']")).sendKeys("Test Asset");
        }

        if (!fieldToSkip.equals("location")) {
            driver.findElement(By.cssSelector("[data-testid='asset-location']")).sendKeys("Room 1");
        }

        if (!fieldToSkip.equals("description")) {
            driver.findElement(By.cssSelector("[data-testid='asset-description']")).sendKeys("Test description");
        }
    }

    private void clickAddAndAssertModalStillOpen() {
        driver.findElement(By.cssSelector("[data-testid='add-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isDisplayed());
    }

    @Test
    void assetAddModalClosesOnCloseButton() {
        openAssetAddModal();

        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Add asset'] button[aria-label='Close']")
        ).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isEmpty());
    }

    @Test
    void assetAddModalSavesValidAsset() {
        openAssetAddModal();

        fillValidAssetExcept("");

        driver.findElement(By.cssSelector("[data-testid='add-asset-button']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isEmpty());
    }

    @Test
    void assetAddModalShowsErrorForEmptyName() {
        openAssetAddModal();
        fillValidAssetExcept("name");
        clickAddAndAssertModalStillOpen();
    }

    @Test
    void assetAddModalShowsErrorForEmptyCategory() {
        openAssetAddModal();
        fillValidAssetExcept("category");
        clickAddAndAssertModalStillOpen();
    }

    @Test
    void assetAddModalShowsErrorForEmptyLocation() {
        openAssetAddModal();
        fillValidAssetExcept("location");
        clickAddAndAssertModalStillOpen();
    }

    @Test
    void assetAddModalShowsErrorForAllFieldsEmpty() {
        openAssetAddModal();

        driver.findElement(By.cssSelector("[data-testid='add-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isDisplayed());
    }
}
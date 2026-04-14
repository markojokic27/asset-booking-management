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

        new Select(driver.findElement(
                By.cssSelector("[data-testid='asset-category']")
        )).selectByIndex(1);

        driver.findElement(By.cssSelector("[data-testid='asset-name']")).sendKeys("Test Asset");
        driver.findElement(By.cssSelector("[data-testid='asset-code']")).sendKeys("QR-001");
        driver.findElement(By.cssSelector("[data-testid='asset-location']")).sendKeys("Room 1");

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

        new Select(driver.findElement(
                By.cssSelector("[data-testid='asset-category']")
        )).selectByIndex(1);
        driver.findElement(By.cssSelector("[data-testid='asset-code']")).sendKeys("QR-001");
        driver.findElement(By.cssSelector("[data-testid='asset-location']")).sendKeys("Room 1");

        driver.findElement(By.cssSelector("[data-testid='add-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isDisplayed());
    }

    @Test
    void assetAddModalShowsErrorForEmptyCategory() {
        openAssetAddModal();

        driver.findElement(By.cssSelector("[data-testid='asset-name']")).sendKeys("Test Asset");
        driver.findElement(By.cssSelector("[data-testid='asset-code']")).sendKeys("QR-001");
        driver.findElement(By.cssSelector("[data-testid='asset-location']")).sendKeys("Room 1");

        driver.findElement(By.cssSelector("[data-testid='add-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isDisplayed());
    }

    @Test
    void assetAddModalShowsErrorForEmptyCode() {
        openAssetAddModal();

        new Select(driver.findElement(
                By.cssSelector("[data-testid='asset-category']")
        )).selectByIndex(1);
        driver.findElement(By.cssSelector("[data-testid='asset-name']")).sendKeys("Test Asset");
        driver.findElement(By.cssSelector("[data-testid='asset-location']")).sendKeys("Room 1");

        driver.findElement(By.cssSelector("[data-testid='add-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isDisplayed());
    }

    @Test
    void assetAddModalShowsErrorForEmptyLocation() {
        openAssetAddModal();

        new Select(driver.findElement(
                By.cssSelector("[data-testid='asset-category']")
        )).selectByIndex(1);
        driver.findElement(By.cssSelector("[data-testid='asset-name']")).sendKeys("Test Asset");
        driver.findElement(By.cssSelector("[data-testid='asset-code']")).sendKeys("QR-001");

        driver.findElement(By.cssSelector("[data-testid='add-asset-button']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Add asset']")
        ).isDisplayed());
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
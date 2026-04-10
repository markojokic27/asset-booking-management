import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import static org.junit.jupiter.api.Assertions.*;

public class AssetModalTest extends BaseTest {

    private void navigateToAssets() {
        WebElement assetsLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/assets']")
                )
        );
        assetsLink.click();
        wait.until(ExpectedConditions.urlContains("http://localhost:5173/assets"));
    }

    private void openAssetViewModal() {
        login();
        navigateToAssets();
        WebElement viewButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='view-asset-button']")
                )
        );
        viewButton.click();
    }

    private void openAssetEditModal() {
        login();
        navigateToAssets();
        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='edit-asset-button']")
                )
        );
        editButton.click();
    }

    // AssetModal (view)

    @Test
    void assetViewModalOpensOnViewButtonClick() {
        openAssetViewModal();
        WebElement modal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[role='dialog'][aria-label='Asset details']")
                )
        );
        assertTrue(modal.isDisplayed());
    }

    @Test
    void assetViewModalClosesOnCloseButton() {
        openAssetViewModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']"))
        );
        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Asset details'] button[aria-label='Close']")
        ).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']"))
        );
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isEmpty());
    }

    // AssetEditModal

    @Test
    void assetEditModalOpens() {
        openAssetEditModal();
        WebElement modal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[role='dialog'][aria-label='Edit asset']")
                )
        );
        assertTrue(modal.isDisplayed());
    }

    @Test
    void assetEditModalClosesOnCloseButton() {
        openAssetEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit asset'] button[aria-label='Close']")
        ).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isEmpty());
    }

    @Test
    void assetEditModalSavesValidChanges() {
        openAssetEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        WebElement nameInput = driver.findElement(By.cssSelector("[name='name']"));
        nameInput.clear();
        nameInput.sendKeys("Dell Latitude 5441");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isEmpty());
        WebElement updatedRow = driver.findElement(
                By.xpath("//table//tbody//tr[td[contains(text(),'Dell Latitude 5441')]]")
        );
        assertTrue(updatedRow.isDisplayed());
    }

    @Test
    void assetEditModalShowsErrorForEmptyName() {
        openAssetEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        WebElement nameInput = driver.findElement(By.cssSelector("[name='name']"));
        nameInput.clear();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isDisplayed());
    }

    @Test
    void assetEditModalShowsErrorForEmptyCategory() {
        openAssetEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        new Select(driver.findElement(By.cssSelector("[name='categoryId']")))
                .selectByValue("");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']")
        ).isDisplayed());
    }

    @Test
    void assetEditModalCanChangeStatus() {
        openAssetEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        new Select(driver.findElement(By.cssSelector("[name='status']")))
                .selectByValue("INACTIVE");
        assertEquals(
                "INACTIVE",
                driver.findElement(By.cssSelector("[name='status']")).getAttribute("value")
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        WebElement statusCell = driver.findElement(
                By.xpath("//table//tbody//tr[1]//td[contains(text(),'INACTIVE')]")
        );
        assertTrue(statusCell.isDisplayed());
    }

    @Test
    void assetEditModalUploadPhotoButtonIsPresent() {
        openAssetEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit asset']"))
        );
        WebElement uploadButton = driver.findElement(
                By.xpath("//button[contains(text(),'photo')]")
        );
        assertTrue(uploadButton.isDisplayed());
    }
}
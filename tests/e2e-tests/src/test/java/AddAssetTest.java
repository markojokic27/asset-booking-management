import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

public class AddAssetTest extends BaseTest {

    private static final String ASSETS_URL = "http://localhost:5173/assets";

    private static final String VALID_NAME = "Test Asset";
    private static final String VALID_LOCATION = "Room 1";
    private static final String VALID_DESCRIPTION = "Test description";
    private static final String LONG_NAME = "t".repeat(101);
    private static final String LONG_LOCATION = "r".repeat(256);
    private static final String LONG_DESCRIPTION = "t".repeat(256);


    private static final By OPEN_MODAL_BUTTON = By.cssSelector("[data-testid='add-asset-button']");
    private static final By MODAL =  By.cssSelector("[data-testid='add-asset-modal']");
    private static final By MODAL_CLOSE = By.cssSelector("[data-testid='close-asset-modal']");
    private static final By CATEGORY_INPUT = By.cssSelector("[data-testid='asset-category']");
    private static final By NAME_INPUT = By.cssSelector("[data-testid='asset-name']");
    private static final By LOCATION_INPUT = By.cssSelector("[data-testid='asset-location']");
    private static final By DESCRIPTION_INPUT = By.cssSelector("[data-testid='asset-description']");
    private static final By SAVE_BUTTON = By.cssSelector("[data-testid='save-asset-button']");

    private void navigateToAssets() {
        driver.get(ASSETS_URL);
        wait.until(ExpectedConditions.elementToBeClickable(OPEN_MODAL_BUTTON));
    }

    private void openAssetAddModal() {
        login();
        navigateToAssets();
        driver.findElement(OPEN_MODAL_BUTTON).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL));
    }

    private void assertStaysOnAssets() {
        wait.until(ExpectedConditions.urlContains("/assets"));
        String currentUrl = driver.getCurrentUrl();
        assertNotNull(currentUrl);
        assertTrue(currentUrl.contains("/assets"));
    }

    private void selectFirstCategory() {
        WebElement category = driver.findElement(CATEGORY_INPUT);
        category.click();
        new Select(category).selectByIndex(1);
    }

    // Close modal

    @Test
    void assetAddModalClosesOnCloseButton() {
        openAssetAddModal();
        driver.findElement(MODAL_CLOSE).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL));
        assertTrue(driver.findElements(MODAL).isEmpty());
    }

    // Add asset with valid data

    @Test
    void assetAddModalSavesValidAsset() {
        openAssetAddModal();
        selectFirstCategory();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(LOCATION_INPUT).sendKeys(VALID_LOCATION);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertEquals(ASSETS_URL, driver.getCurrentUrl());
    }

    // Add asset with empty fields

    @Test
    void assetAddModalShowsErrorForEmptyName() {
        openAssetAddModal();
        selectFirstCategory();
        driver.findElement(LOCATION_INPUT).sendKeys(VALID_LOCATION);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnAssets();
    }

    @Test
    void assetAddModalShowsErrorForEmptyCategory() {
        openAssetAddModal();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(LOCATION_INPUT).sendKeys(VALID_LOCATION);
        driver.findElement(LOCATION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnAssets();
    }

    @Test
    void assetAddModalShowsErrorForEmptyLocation() {
        openAssetAddModal();
        selectFirstCategory();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnAssets();
    }

    @Test
    void assetAddModalSavesValidAssetForDescription() {
        openAssetAddModal();
        selectFirstCategory();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(LOCATION_INPUT).sendKeys(VALID_LOCATION);
        driver.findElement(SAVE_BUTTON).click();
        assertEquals(ASSETS_URL, driver.getCurrentUrl());
    }

    @Test
    void assetAddModalShowsErrorForAllFieldsEmpty() {
        openAssetAddModal();
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnAssets();
    }

    // Create asset with long fields

    @Test
    void assetAddWithLongName() {
        openAssetAddModal();
        selectFirstCategory();
        driver.findElement(NAME_INPUT).sendKeys(LONG_NAME);
        driver.findElement(LOCATION_INPUT).sendKeys(VALID_LOCATION);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnAssets();
    }

    @Test
    void assetAddWithLongLocation() {
        openAssetAddModal();
        selectFirstCategory();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(LOCATION_INPUT).sendKeys(LONG_LOCATION);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnAssets();
    }

    @Test
    void assetAddWithLongDescription() {
        openAssetAddModal();
        selectFirstCategory();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(LOCATION_INPUT).sendKeys(VALID_LOCATION);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(LONG_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertEquals(ASSETS_URL, driver.getCurrentUrl());
    }


}
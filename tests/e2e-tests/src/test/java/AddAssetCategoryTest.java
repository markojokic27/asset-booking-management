import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AddAssetCategoryTest extends BaseTest {

    private static final String CATEGORIES_URL = "http://localhost:5173/categories";
    private static final String VALID_NAME = "Test Category";
    private static final String VALID_DESCRIPTION = "Test Description";
    private static final String LONG_NAME = "t".repeat(101);
    private static final String LONG_DESCRIPTION  = "t".repeat(256);

    private static final By OPEN_MODAL_BUTTON = By.cssSelector("[data-testid='add-category-button']");
    private static final By MODAL = By.cssSelector("[role='dialog']");
    private static final By MODAL_CLOSE = By.cssSelector("[data-testid='category-close-button']");
    private static final By NAME_INPUT = By.cssSelector("[data-testid='category-name']");
    private static final By DESCRIPTION_INPUT = By.cssSelector("[data-testid='category-description']");
    private static final By SAVE_BUTTON = By.cssSelector("[data-testid='save-category-button']");

    private void navigateToCategories() {
        driver.get(CATEGORIES_URL);
        wait.until(ExpectedConditions.elementToBeClickable(OPEN_MODAL_BUTTON));
    }

    private void openAddCategoryModal() {
        login();
        navigateToCategories();

        driver.findElement(OPEN_MODAL_BUTTON).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL));
    }

    private void assertStaysOnCategories() {
        wait.until(ExpectedConditions.urlContains("/categories"));
        String currentUrl = driver.getCurrentUrl();
        assertNotNull(currentUrl);
        assertTrue(currentUrl.contains("/categories"));
    }

    // Open and close modal

    @Test
    void addCategoryModalClosesOnCloseButton() {
        openAddCategoryModal();
        driver.findElement(MODAL_CLOSE).click();

        assertTrue(driver.findElements(MODAL).isEmpty());
    }

    // Add category with valid data

    @Test
    void addCategoryWithValidData() {
        openAddCategoryModal();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertEquals(CATEGORIES_URL, driver.getCurrentUrl());
    }

    @Test
    void addCategoryWithoutDescription() {
        openAddCategoryModal();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(SAVE_BUTTON).click();
        assertEquals(CATEGORIES_URL, driver.getCurrentUrl());

    }

    // Add category with empty fields

    @Test
    void addCategoryWithEmptyName() {
        openAddCategoryModal();
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnCategories();
    }

    @Test
    void addCategoryWithAllFieldsEmpty() {
        openAddCategoryModal();
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnCategories();
    }

    // Add category with long fields

    @Test
    void addCategoryWithLongName() {
        openAddCategoryModal();
        driver.findElement(NAME_INPUT).sendKeys(LONG_NAME);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(VALID_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnCategories();
    }

    @Test
    void addCategoryWithLongDescription() {
        openAddCategoryModal();
        driver.findElement(NAME_INPUT).sendKeys(VALID_NAME);
        driver.findElement(DESCRIPTION_INPUT).sendKeys(LONG_DESCRIPTION);
        driver.findElement(SAVE_BUTTON).click();
        assertStaysOnCategories();
    }
}
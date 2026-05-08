import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

public class AssetCategoryEditModalTest extends BaseTest {

    private void navigateToCategories() {
        login();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/categories']")
        )).click();
        wait.until(ExpectedConditions.urlContains("/categories"));
    }

    private void openEditModal() {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table tbody tr")
        ));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("table tbody tr:first-child button:last-child")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog']")
        ));
    }

    private void assertModalOpen() {
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog']")
        ).isDisplayed());
    }

    private void assertModalClosed() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog']")
        ));
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog']")
        ).isEmpty());
    }
    private void clickSave() {
        driver.findElement(By.cssSelector("[data-testid='save-category-button']")).click();
    }

    @Test
    void editCategoryModalClosesOnCloseButton() {
        navigateToCategories();
        openEditModal();

        driver.findElement(
                By.cssSelector("[role='dialog'] button[type='button']")
        ).click();

        assertModalClosed();
    }

    @Test
    void editCategoryModalSavesValidChanges() {
        navigateToCategories();
        openEditModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='edit-category-name']"));
        nameInput.clear();
        nameInput.sendKeys("Laptops");

        WebElement descriptionInput = driver.findElement(By.cssSelector("[data-testid='edit-category-description']"));
        descriptionInput.clear();
        descriptionInput.sendKeys("Updated Description");

        new Select(driver.findElement(By.cssSelector("[data-testid='edit-category-booking-period']")))
                .selectByValue("WEEK");

        clickSave();
        assertModalClosed();
    }

    @Test
    void editCategoryModalShowsErrorForEmptyName() {
        navigateToCategories();
        openEditModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='edit-category-name']"));
        nameInput.clear();

        clickSave();
        assertModalOpen();
    }
}
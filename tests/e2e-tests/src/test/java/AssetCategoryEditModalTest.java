import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("table tbody tr:first-child button:last-child")
                )
        );
        editButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit category']")
        ));
    }

    private void clickSave() {
        driver.findElement(By.cssSelector("[data-testid='save-category-button']")).click();
    }

    private void assertModalOpen() {
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit category']")
        ).isDisplayed());
    }

    private void assertModalClosed() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit category']")
        ));
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit category']")
        ).isEmpty());
    }

    @Test
    void editCategoryModalClosesOnCloseButton() {
        login();
        navigateToCategories();
        openEditModal();

        driver.findElement(By.cssSelector("[data-testid='edit-category-close-button']")).click();

        assertModalClosed();
    }

    @Test
    void editCategoryModalSavesValidChanges() {
        login();
        navigateToCategories();
        openEditModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='edit-category-name']"));
        nameInput.clear();
        nameInput.sendKeys("Laptops");

        WebElement descriptionInput = driver.findElement(By.cssSelector("[data-testid='edit-category-description']"));
        descriptionInput.clear();
        descriptionInput.sendKeys("Updated Description");

        WebElement bookingPeriodDropdown = driver.findElement(
                By.cssSelector("[data-testid='edit-category-booking-period']")
        );
        bookingPeriodDropdown.click();

        WebElement weekOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='edit-category-booking-period'] option[value='WEEK']")
                )
        );
        weekOption.click();

        clickSave();
        assertModalClosed();
    }

    @Test
    void editCategoryModalShowsErrorForEmptyName() {
        login();
        navigateToCategories();
        openEditModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='edit-category-name']"));
        nameInput.clear();

        clickSave();
        assertModalOpen();
    }
}
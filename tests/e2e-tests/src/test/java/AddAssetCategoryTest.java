import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AddAssetCategoryTest extends BaseTest {

    private void navigateToCategories() {
        login();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/categories']")
        )).click();
        wait.until(ExpectedConditions.urlContains("/categories"));
    }

    private void openAddCategoryModal() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'New') or contains(text(), 'Add')]")
        )).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ));
    }

    private void assertModalClosed() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ));
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isEmpty());
    }

    @Test
    void addCategoryModalOpens() {
        navigateToCategories();
        openAddCategoryModal();

        assertTrue(driver.findElement(
                By.cssSelector("[data-testid='category-name']")
        ).isDisplayed());
    }

    @Test
    void addCategoryModalClosesOnXButton() {
        navigateToCategories();
        openAddCategoryModal();

        driver.findElement(By.cssSelector("[data-testid='category-close-button']")).click();

        assertModalClosed();
    }

    @Test
    void addCategorySubmitWithoutNameShowsError() {
        navigateToCategories();
        openAddCategoryModal();

        driver.findElement(By.cssSelector("[data-testid='add-category-button']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Name is required')]")
        ));

        assertTrue(driver.findElement(
                By.xpath("//*[contains(text(),'Name is required')]")
        ).isDisplayed());
    }

    @Test
    void addCategorySuccess() {
        navigateToCategories();
        openAddCategoryModal();

        WebElement nameInput = driver.findElement(By.cssSelector("[data-testid='category-name']"));
        nameInput.click();
        nameInput.sendKeys("Test Category");

        WebElement descriptionInput = driver.findElement(By.cssSelector("[data-testid='category-description']"));
        descriptionInput.click();
        descriptionInput.sendKeys("Test Description");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='add-category-button']")
        )).click();

        assertModalClosed();
    }

}
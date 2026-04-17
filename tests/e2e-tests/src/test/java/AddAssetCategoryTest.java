import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.junit.jupiter.api.Assertions.*;

public class AddAssetCategoryTest extends BaseTest {

    private void navigateToCategories() {
        WebElement categoriesLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/categories']")
                )
        );
        categoriesLink.click();
        wait.until(ExpectedConditions.urlContains("/categories"));
    }

    private void openCategoryCreateModal() {
        login();
        navigateToCategories();
        WebElement createButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'Add new category')]")
                )
        );
        createButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ));
    }

    @Test
    void categoryCreateModalClosesOnCloseButton() {
        openCategoryCreateModal();
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

    @Test
    void categoryCreateModalSavesValidCategory() {
        openCategoryCreateModal();
        driver.findElement(By.cssSelector("[data-testid='category-name']")).sendKeys("Laptops");
        driver.findElement(By.cssSelector("[data-testid='category-description']")).sendKeys("All company laptops");
        driver.findElement(By.cssSelector("[data-testid='category-booking-period']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[@value='1']")
        )).click();
        driver.findElement(By.cssSelector("[data-testid='category-approval-checkbox']")).click();
        driver.findElement(By.cssSelector("[data-testid='add-category-button']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ));
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isEmpty());
    }

    @Test
    void categoryCreateModalSavesWithoutDescription() {
        openCategoryCreateModal();
        driver.findElement(By.cssSelector("[data-testid='category-name']")).sendKeys("Laptops");
        driver.findElement(By.cssSelector("[data-testid='category-booking-period']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[@value='1']")
        )).click();
        driver.findElement(By.cssSelector("[data-testid='category-approval-checkbox']")).click();
        driver.findElement(By.cssSelector("[data-testid='add-category-button']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ));
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isEmpty());
    }

    @Test
    void categoryCreateModalTogglesApprovalCheckbox() {
        openCategoryCreateModal();
        WebElement checkbox = driver.findElement(
                By.cssSelector("[data-testid='category-approval-checkbox']")
        );
        assertFalse(checkbox.isSelected());
        checkbox.click();
        assertTrue(checkbox.isSelected());
        checkbox.click();
        assertFalse(checkbox.isSelected());
    }

    @Test
    void categoryCreateModalShowsErrorForEmptyName() {
        openCategoryCreateModal();
        driver.findElement(By.cssSelector("[data-testid='category-description']")).sendKeys("Some description");
        driver.findElement(By.cssSelector("[data-testid='category-booking-period']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[@value='1']")
        )).click();
        driver.findElement(By.cssSelector("[data-testid='category-approval-checkbox']")).click();
        driver.findElement(By.cssSelector("[data-testid='add-category-button']")).click();
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Category name is required')]")
        )).isDisplayed());
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isDisplayed());
    }

    @Test
    void categoryCreateModalShowsErrorForEmptyBookingPeriod() {
        openCategoryCreateModal();
        driver.findElement(By.cssSelector("[data-testid='category-name']")).sendKeys("Laptops");
        driver.findElement(By.cssSelector("[data-testid='category-approval-checkbox']")).click();
        driver.findElement(By.cssSelector("[data-testid='add-category-button']")).click();
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Booking period is required')]")
        )).isDisplayed());
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isDisplayed());
    }

    @Test
    void categoryCreateModalShowsErrorForAllFieldsEmpty() {
        openCategoryCreateModal();
        driver.findElement(By.cssSelector("[data-testid='add-category-button']")).click();
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Category name is required')]")
        )).isDisplayed());
        driver.findElement(By.cssSelector("[data-testid='category-approval-checkbox']")).click();

        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Asset details']")
        ).isDisplayed());
    }
}
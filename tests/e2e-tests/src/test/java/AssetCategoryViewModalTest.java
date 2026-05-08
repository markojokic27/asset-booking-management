import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AssetCategoryViewModalTest extends BaseTest {

    private void navigateToCategories() {
        login();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/categories']")
        )).click();
        wait.until(ExpectedConditions.urlContains("/categories"));
    }

    private void openViewModal() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("tbody tr")
        ));

        driver.findElements(By.cssSelector("tbody tr")).get(0)
                .findElement(By.cssSelector("button:first-of-type"))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='category-name']")
        ));
    }

    @Test
    void viewCategoryModalShowsData() {
        navigateToCategories();
        openViewModal();

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='category-name']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='category-bookingPeriod']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='category-approval']")
        ).getText().isBlank());
    }



    @Test
    void viewCategoryModalClosesOnXButton() {
        navigateToCategories();
        openViewModal();

        driver.findElement(
                By.cssSelector("[role='dialog'] button")
        ).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[data-testid='category-name']")
        ));

        assertTrue(driver.findElements(
                By.cssSelector("[data-testid='category-name']")
        ).isEmpty());
    }
}
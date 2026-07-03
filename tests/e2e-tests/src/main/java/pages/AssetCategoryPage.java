package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class AssetCategoryPage extends CommonMethods {

    public AssetCategoryPage() {
        super();
    }

    // Locators - Add category
    public By openCategoryModal = By.cssSelector("[data-testid='add-category-button']");
    public By categoryModal = By.cssSelector("[data-testid='category-modal']");
    public By categoryCloseModal = By.cssSelector("[data-testid='category-close-button']");
    public By categoryNameField = By.cssSelector("[data-testid='category-name']");
    public By categoryDescriptionField = By.cssSelector("[data-testid='category-description']");
    public By categoryBookingPeriodField = By.cssSelector("[data-testid='category-booking-period']");
    public By categoryApprovalField = By.cssSelector("[data-testid='category-approval-checkbox']");
    public By categorySaveButton = By.cssSelector("[data-testid='save-category-button']");

    // Locators - View category
    public By assetCategoryViewOpenModal = By.cssSelector("[data-testid='view-assetCategory-button']");
    public By assetCategoryViewCloseModal = By.cssSelector("[data-testid='category-close-button']");

    // Locators - Edit category
    public By assetCategoryEditOpenModal = By.cssSelector("[data-testid='edit-assetCategory-button']");
    public By assetCategoryEditModal = By.cssSelector("[data-testid='assetCategory-modal']");
    public By assetCategoryEditCloseModal = By.cssSelector("[data-testid='category-close-modal']");
    public By categoryEditNameField = By.cssSelector("[data-testid='edit-category-name']");
    public By categoryEditDescriptionField = By.cssSelector("[data-testid='edit-category-description']");
    public By categoryEditBookingPeriodField = By.cssSelector("[data-testid='edit-category-booking-period']");
    public By categoryEditApprovalField = By.cssSelector("[data-testid='edit-category-approval-checkbox']");
    public By editCategoryButton = By.cssSelector("[data-testid='save-category-button']");

    // Locators - Search
    public By searchField = By.cssSelector("[data-testid='search-input']");


    public void closeAnyOpenModal() {
        By overlay = By.cssSelector("button[aria-label='Close dialog']");
        List<WebElement> overlays = driver.findElements(overlay);
        if (!overlays.isEmpty() && overlays.getFirst().isDisplayed()) {
            jsClick(overlays.getFirst());
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        }
    }

    // Add category
    public void openCategoryModal() {
        closeAnyOpenModal();
        clickOnElement(openCategoryModal);
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryModal));
    }

    public void closeCategoryModal() {
        clickOnElement(categoryCloseModal);
    }

    public void clickCategoryButton() {
        clickOnElement(categorySaveButton);
    }

    public void typeName(String name) {
        typeInElement(categoryNameField, name);
    }

    public void typeDescription(String description) {
        typeInElement(categoryDescriptionField, description);
    }

    public void typeBookingPeriod(String bookingPeriod) {
        selectByVisibleText(categoryBookingPeriodField, bookingPeriod);
    }

    public void clickCategoryApproval() {
        WebElement input = driver.findElement(categoryApprovalField);
        jsClick(input);
    }

    public void category(String name, String description, String bookingPeriod) {
        typeName(name);
        typeDescription(description);
        typeBookingPeriod(bookingPeriod);
        clickCategoryButton();
    }


    // View category
    public void assetCategoryViewOpenModal() {
        closeAnyOpenModal();
        clickOnElement(assetCategoryViewOpenModal);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='category-name']")));
    }

    public void assetCategoryViewCloseModal() {
        clickOnElement(assetCategoryViewCloseModal);
    }


    // Edit category
    public void assetCategoryEditOpenModal() {
        closeAnyOpenModal();
        clickOnElement(assetCategoryEditOpenModal);
        wait.until(ExpectedConditions.visibilityOfElementLocated(assetCategoryEditModal));
    }

    public void assetCategoryEditCloseModal() {
        clickOnElement(assetCategoryEditCloseModal);
    }

    public void typeEditName(String name) {
        typeInElement(categoryEditNameField, name);
    }

    public void typeEditDescription(String description) {
        typeInElement(categoryEditDescriptionField, description);
    }

    public void typeEditBookingPeriod(String bookingPeriod) {
        selectByVisibleText(categoryEditBookingPeriodField, bookingPeriod);
    }

    public void clickEditCategoryApproval() {
        WebElement input = driver.findElement(categoryEditApprovalField);
        jsClick(input);
    }

    public void clickEditCategoryButton() {
        clickOnElement(editCategoryButton);
    }
    public void editCategory(String name, String description, String bookingPeriod) {
        typeEditName(name);
        typeEditDescription(description);
        typeEditBookingPeriod(bookingPeriod);
        clickEditCategoryButton();
    }


    // Search
    public void searchCategoryAssets(String categoryAssets) {
        typeInElement(searchField, categoryAssets);
    }
}
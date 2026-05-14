package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class AssetCategoryPage extends CommonMethods {

    public AssetCategoryPage(){
        super();
    }

    public By openCategoryModal = By.cssSelector("[data-testid='add-category-button']");
    public By categoryModal = By.cssSelector("[data-testid='category-modal']");
    public By categoryCloseModal = By.cssSelector("[data-testid='category-close-button']");
    public By categoryNameField = By.cssSelector("[data-testid='category-name']");
    public By categoryDescriptionField = By.cssSelector("[data-testid='category-description']");
    public By categoryBookingPeriodField = By.cssSelector("[data-testid='category-booking-period']");
    public By categoryApprovalField = By.cssSelector("[data-testid='category-approval-checkbox']");
    public By categorySaveButton = By.cssSelector("[data-testid='save-category-button']");

    public void openCategoryModal(){
        clickOnElement(openCategoryModal);
    }

    public void closeCategoryModal(){
        clickOnElement(categoryCloseModal);
    }

    public void typeName(String name){
        typeInElement(categoryNameField, name);
    }

    public void typeDescription(String description){
        typeInElement(categoryDescriptionField, description);
    }

    public void typeBookingPeriod(String bookingPeriod){
        typeInElement(categoryBookingPeriodField, bookingPeriod);
    }

    public void clickCategoryApproval(){
        clickOnElement(categoryApprovalField);
    }

    public void clickCategoryButton(){
        clickOnElement(categorySaveButton);
    }

    public void category(String name, String description, String bookingPeriod){
        typeName(name);
        typeDescription(description);
        typeBookingPeriod(bookingPeriod);
        clickCategoryButton();
    }


}

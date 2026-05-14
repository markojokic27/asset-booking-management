package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class AssetPage extends CommonMethods {

    public AssetPage(){
        super();
    }

    public By assetOpenModal = By.cssSelector("[data-testid='add-asset-button']");
    public By assetModal = By.cssSelector("[data-testid='add-asset-modal']");
    public By assetCloseModal = By.cssSelector("[data-testid='close-asset-modal']");
    public By assetStatus= By.cssSelector("[data-testid='asset-status']");
    public By assetCategory = By.cssSelector("[data-testid='asset-category']");
    public By assetNameField = By.cssSelector("[data-testid='asset-name']");
    public By assetLocationField = By.cssSelector("[data-testid='asset-location']");
    public By assetDescriptionField = By.cssSelector("[data-testid='asset-description']");
    public By assetButton = By.cssSelector("[data-testid='save-asset-button']");


    public void assetOpenModal() {
        clickOnElement(assetOpenModal);
    }

    public void assetCloseModal(){
        clickOnElement(assetCloseModal);
    }

    public void selectStatus(String status){
        selectByVisibleText(assetStatus, status);
    }

    public void selectCategory(String category){
        clickOnElement(assetCategory);
        selectByVisibleText(assetCategory, category);
    }

    public void typeName(String name){
        typeInElement(assetNameField, name);
    }

    public void typeLocation(String location){
        typeInElement(assetLocationField, location);
    }

    public void typeDescription(String description){
        typeInElement(assetDescriptionField, description);
    }

    public void clickAssetButton(){
        clickOnElement(assetButton);
    }

    public void asset(String status, String category, String name, String location, String description){
        selectStatus(status);
        selectCategory(category);
        typeName(name);
        typeLocation(location);
        typeDescription(description);
        clickAssetButton();
    }

}
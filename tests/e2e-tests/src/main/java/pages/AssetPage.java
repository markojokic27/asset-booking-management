package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class AssetPage extends CommonMethods {

    public AssetPage(){
        super();
    }

    // Add asset
    public By assetOpenModal = By.cssSelector("[data-testid='add-asset-button']");
    public By assetModal = By.cssSelector("[data-testid='add-asset-modal']");
    public By assetCloseModal = By.cssSelector("[data-testid='close-asset-modal']");

    // Edit asset
    public By assetEditOpenModal = By.cssSelector("[data-testid='edit-asset-button']");
    public By assetEditModal = By.cssSelector("[data-testid='edit-asset-modal']");
    public By assetEditCloseModal = By.cssSelector("[data-testid='close-edit-modal']");

    //View asset
    public By assetViewOpenModal = By.cssSelector("[data-testid='view-asset-button']");
    public By assetViewModal = By.cssSelector("[data-testid='asset-view-modal']");
    public By assetViewCloseModal = By.cssSelector("[data-testid='asset-details-close-button']");

    // Delete asset
    public By asseDeleteOpenModal = By.cssSelector("[data-testid='delete-asset-button']");
    public By cancelDeleteButton = By.cssSelector("[data-testid='cancel-delete-button']");
    public By confirmDeleteButton = By.cssSelector("[data-testid='confirm-delete-button']");

    // View booking asset
    public By assetBookingOpenModal = By.cssSelector("[data-testid='asset-bookings-button']");
    public By assetBookingCloseModal = By.cssSelector("[data-testid='close-asset-bookings-modal']");

    public By assetStatus= By.cssSelector("[data-testid='asset-status']");
    public By assetCategory = By.cssSelector("[data-testid='asset-category']");
    public By assetNameField = By.cssSelector("[data-testid='asset-name']");
    public By assetLocationField = By.cssSelector("[data-testid='asset-location']");
    public By assetDescriptionField = By.cssSelector("[data-testid='asset-description']");
    public By assetButton = By.cssSelector("[data-testid='save-asset-button']");
    public By editButton = By.cssSelector("[data-testid='save-edit-button']");

    // Search assets
    public By searchField = By.cssSelector("[data-testid='search-input']");
    public By filterStatus= By.cssSelector("[data-testid='asset-status-filter']");


    // Toggle deleted assets
    public By toggleDeletedAssets = By.cssSelector("[data-testid='toggle-deleted']");

    // Asset category filter
    public By laptopCategoryCard = By.cssSelector("[data-testid='category-card-laptop']");


    // Form actions
    public void selectStatus(String status) {
        selectByVisibleText(assetStatus, status);
    }

    public void selectCategory(String category) {
        if (category == null || category.isEmpty()) return;
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

    private void fillForm(String status, String category, String name, String location, String description) {
        selectStatus(status);
        selectCategory(category);
        typeName(name);
        typeLocation(location);
        typeDescription(description);
    }


    // Add asset
    public void assetOpenModal() {
        clickOnElement(assetOpenModal);
    }

    public void assetCloseModal(){
        clickOnElement(assetCloseModal);
    }

    public void asset(String status, String category, String name, String location, String description){
        fillForm(status, category, name, location, description);
        clickOnElement(assetButton);
    }


    // Edit asset
    public void assetEditOpenModal() {
        clickOnElement(assetEditOpenModal);
    }

    public void assetEditCloseModal() {
        clickOnElement(assetEditCloseModal);
    }

    public void clickEditButton(){
        clickOnElement(editButton);
    }

    public void editAsset(String status, String category, String name, String location, String description){
        fillForm(status, category, name, location, description);
        clickOnElement(editButton);
    }


    // View asset
    public void assetViewOpenModal() {
        clickOnElement(assetViewOpenModal);
    }

    public void assetViewCloseModal() {
        clickOnElement(assetViewCloseModal);
    }


    // Delete asset
    public void assetDeleteOpenModal() {
        clickOnElement(asseDeleteOpenModal);
    }

    public void cancelDeleteButton(){
        clickOnElement(cancelDeleteButton);
    }

    public void confirmDeleteButton(){
        clickOnElement(confirmDeleteButton);
    }


    // View booking for asset
    public void assetBookingOpenModal() {
        clickOnElement(assetBookingOpenModal);
    }

    public void assetBookingCloseModal() {
        clickOnElement(assetBookingCloseModal);
    }


    // Toggle deleted assets
    public void clickToggleDeletedAssets() {
        clickOnElement(toggleDeletedAssets);
    }

    // Search assets
    public void searchAssets(String assets){
        typeInElement(searchField, assets);
    }
    public void selectAssetStatus(String status){selectByVisibleText(filterStatus, status);}

    // Asset category filter
    public void clickLaptopCategory() {
        clickOnElement(laptopCategoryCard);
    }
}
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
    public By assetEditOpenModal = By.cssSelector("[ data-testid='edit-asset-button']");
    public By assetEditModal = By.cssSelector("[data-testid='asset-modal']");
    public By assetEditCloseModal = By.cssSelector("[data-testid='close-modal']");

    //View asset
    public By assetViewOpenModal = By.cssSelector("[data-testid='view-asset-button']");
    public By assetViewModal = By.cssSelector("[data-testid='asset-view-modal']");
    public By assetViewCloseModal = By.cssSelector("[data-testid='asset-details-close-button']");

    //Report asset

    public By assetReportOpenModal = By.cssSelector("[data-testid='report-asset-button']");
    public By assetReportCloseModal = By.cssSelector("[data-testid='close-view-modal']");

    // Delete asset

    public By asseDeleteOpenModal = By.cssSelector("[ data-testid='delete-asset-button']");
    public By cancelDeleteButton = By.cssSelector("[data-testid='cancel-delete-button']");
    public By confirmDeleteButton = By.cssSelector("[data-testid='confirm-delete-button']");

    // View booking asset

    public By assetBookingOpenModal = By.cssSelector("[data-testid='asset-bookings-button']");
    public By assetBookingCloseModal = By.cssSelector("[data-testid='close-asset-bookings-modal']");
    public By assetBookingModal = By.cssSelector("[data-testid='asset-view-modal']");

    public By assetStatus= By.cssSelector("[data-testid='asset-status']");
    public By assetCategory = By.cssSelector("[data-testid='asset-category']");
    public By assetNameField = By.cssSelector("[data-testid='asset-name']");
    public By assetLocationField = By.cssSelector("[data-testid='asset-location']");
    public By assetDescriptionField = By.cssSelector("[data-testid='asset-description']");
    public By assetButton = By.cssSelector("[data-testid='save-asset-button']");
    public By editButton = By.cssSelector("[data-testid='save-edit-button']");


    // Add asset
    public void assetOpenModal() {
        clickOnElement(assetOpenModal);
    }

    public void assetCloseModal(){
        clickOnElement(assetCloseModal);
    }
    public void clickAssetButton(){
        clickOnElement(assetButton);
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

    // View asset
    public void assetViewOpenModal() {
        clickOnElement(assetViewOpenModal);
    }

    public void assetViewCloseModal() {
        clickOnElement(assetViewCloseModal);
    }

    //Report asset
    public void assetReportOpenModal() {
        clickOnElement(assetReportOpenModal);
    }

    public void assetReportCloseModal() {
        clickOnElement(assetReportCloseModal);
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

    // View booking asset

    public void assetBookingOpenModal() {
        clickOnElement(assetBookingOpenModal);
    }

    public void assetBookingCloseModal() {
        clickOnElement(assetBookingCloseModal);
    }



    public void asset(String status, String category, String name, String location, String description){
        selectStatus(status);
        selectCategory(category);
        typeName(name);
        typeLocation(location);
        typeDescription(description);
        clickAssetButton();
    }

    public void editAsset(String status, String category, String name, String location, String description){
        selectStatus(status);
        selectCategory(category);
        typeName(name);
        typeLocation(location);
        typeDescription(description);
        clickEditButton();
    }

}
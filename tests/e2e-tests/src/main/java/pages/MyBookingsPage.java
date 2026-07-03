package pages;


import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class MyBookingsPage extends CommonMethods {

    public MyBookingsPage() {
        super();
    }
    public By bookingList = By.cssSelector("table");
    public By assetFilter = By.cssSelector("[data-testid='my-booking-asset-filter']");
    public By searchField = By.cssSelector("[data-testid='search-input']");
    public By inputFromDate = By.id("my-bookings-from-date");
    public By inputToDate   = By.id("my-bookings-to-date");

    public By firstCancelButton = By.cssSelector("[data-testid^='cancel-booking-']");
    public By cancelBookingModal = By.cssSelector("[role='dialog']");
    public By confirmCancelButton = By.cssSelector("[data-testid='confirm-cancel-booking-button']");
    public By keepBookingButton = By.cssSelector("[data-testid='keep-booking-button']");

    public By selectMyBookingStatus = By.cssSelector("[data-testid='my-booking-status-filter']");
    public By selectMyBookingAsset = By.cssSelector("[data-testid='my-booking-asset-filter']");
    public void clickFirstCancelButton() {
        clickOnElement(firstCancelButton);
    }

    public void confirmCancel() {
        clickOnElement(confirmCancelButton);
    }

    public void keepBooking() {
        clickOnElement(keepBookingButton);
    }


    public void selectAssetFilter(String asset){
        selectByVisibleText(assetFilter, asset);
    }
    public void searchAssets(String keyword) {
        typeInElement(searchField, keyword);
    }

    public void inputFromDate(String date){
        inputDate(inputFromDate, date);
    }

    public void inputToDate(String date){
        inputDate(inputToDate, date);
    }

    public void selectMyBookingStatus(String status){selectByVisibleText(selectMyBookingStatus, status);}
    public void selectMyBookingAsset(String asset){selectByVisibleText(selectMyBookingAsset, asset);}

}

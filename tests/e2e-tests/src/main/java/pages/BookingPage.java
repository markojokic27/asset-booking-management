package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class BookingPage extends CommonMethods {

    public BookingPage(){
        super();
    }

    public By BookingsButton = By.cssSelector("[data-testid='booking-button']");

    // Search assets
    public By searchField = By.cssSelector("[data-testid='search-input']");

    public void clickBookingButton(){
        clickOnElement(BookingsButton);
    }


    // Search assets
    public void searchAssets(String assets){
        typeInElement(searchField, assets);
    }


}

package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class BookingPage extends CommonMethods {

    public BookingPage(){
        super();
    }

    public By BookButton = By.cssSelector("[data-testid='book-button']");

    // Search assets
    public By searchField = By.cssSelector("[data-testid='search-input']");

    public void clickBookButton(){
        clickOnElement(BookButton);
    }


    // Search assets
    public void searchAssets(String assets){
        typeInElement(searchField, assets);
    }


}

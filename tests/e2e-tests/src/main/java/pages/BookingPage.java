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

    // Asset category filter
    public By laptopCategoryCard = By.cssSelector("[data-testid='category-card-laptop']");

    public void clickBookButton(){
        clickOnElement(BookButton);
    }


    // Search assets
    public void searchAssets(String assets){
        typeInElement(searchField, assets);
    }

    // Asset category filter

    public void clickLaptopCategory() {
        clickOnElement(laptopCategoryCard);
    }





}

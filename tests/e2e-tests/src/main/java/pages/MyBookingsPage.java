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


    public void selectAssetFilter(String asset){
        selectByVisibleText(assetFilter, asset);
    }
    public void searchAssets(String keyword) {
        typeInElement(searchField, keyword);
    }


}

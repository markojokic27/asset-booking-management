package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class ReportPage extends CommonMethods {

    public ReportPage(){
        super();
    }

    public By viewBookingByStatus = By.cssSelector("[data-testid='booking-by-status']");

    public By viewBookingByMonth = By.cssSelector("[data-testid='booking-by-month']");
    public By viewTopAssetsByNumberOfBookings = By.cssSelector("[data-testid='top-assets']");
    public By viewTopUsersByNumberOfBookings = By.cssSelector("[data-testid='top-users']");
    public void viewBookingByStatus() {
        scrollToElement(viewBookingByStatus);
        isElementVisible(viewBookingByStatus);
    }

    public void viewBookingByMonth() {
        scrollToElement(viewBookingByMonth);
        isElementVisible(viewBookingByMonth);
    }

    public void viewTopAssets(){
        scrollToElement(viewTopAssetsByNumberOfBookings);
        isElementVisible(viewTopAssetsByNumberOfBookings);
    }

    public void viewTopUsers(){
        scrollToElement(viewTopUsersByNumberOfBookings);
        isElementVisible(viewTopUsersByNumberOfBookings);
    }

}

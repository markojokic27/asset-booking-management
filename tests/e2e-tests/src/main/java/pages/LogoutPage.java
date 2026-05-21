package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class LogoutPage extends CommonMethods {

    public LogoutPage(){
        super();
    }

    public By logoutButton = By.cssSelector("nav a[href='/login']");

    public void clickLogoutButton(){
        clickOnElement(logoutButton);
    }


}

package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;


public class LoginPage extends CommonMethods {

    public LoginPage() {
        super();
    }

    public By loginButton = By.cssSelector("[data-testid='login-button']");
    public By userNameField = By.cssSelector("[data-testid='username']");
    public By passwordField = By.cssSelector("[data-testid='password']");

    public void clickLoginButton() {
        clickOnElement(loginButton);
    }

    public void typePassword(String password) {
        typeInElement(passwordField, password);
    }

    public void typeUsername(String username) {
        typeInElement(userNameField, username);
    }

    public void login(String username, String password){
        typeUsername(username);
        typePassword(password);
        clickLoginButton();
    }


}

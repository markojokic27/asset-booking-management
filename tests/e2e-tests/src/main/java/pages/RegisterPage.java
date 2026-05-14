package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class RegisterPage  extends CommonMethods {

    public RegisterPage(){
        super();
    }

    public By userNameField = By.cssSelector("[data-testid='name']");
    public By userSurnameField = By.cssSelector("[data-testid='surname']");
    public By userUsernameField= By.cssSelector("[data-testid='username']");
    public By userPasswordField = By.cssSelector("[data-testid='password']");
    public By registerButton  = By.cssSelector("[data-testid='register-button']");

    public void clickRegisterButton(){

        clickOnElement(registerButton);
    }

    public void typeName(String name){
        typeInElement(userNameField, name);
    }

    public void typeSurname(String surname){
        typeInElement(userSurnameField, surname);
    }

    public void typeUsername(String username){
        typeInElement(userUsernameField, username);
    }

    public void typePassword(String password){
        typeInElement(userPasswordField, password);
    }

    public void register(String name, String surname, String username, String password){
        typeName(name);
        typeSurname(surname);
        typeUsername(username);
        typePassword(password);
        clickRegisterButton();
    }
}
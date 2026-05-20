package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class AccountPage extends CommonMethods {

    public AccountPage(){
        super();
    }

    // Account info

    public By openHeadingModal = By.cssSelector("[data-testid='account-heading']");
    public By typeCurrentPassword = By.cssSelector("[data-testid='account-password-current']");
    public By typeNewPassword = By.cssSelector("[data-testid='account-password-new']");
    public By typeConfirmPassword = By.cssSelector("[data-testid='account-password-confirm']");
    public By saveNewPassword = By.cssSelector("[data-testid='account-password-submit']");
    public By changePassword = By.cssSelector("[data-testid='account-open-change-password']");
    public void openHeadingModal (){
            clickOnElement(openHeadingModal);
    }

    public void typeCurrentPassword(String currentPassword){
        typeInElement(typeCurrentPassword, currentPassword);
    }

    public void typeNewPassword(String newPassword){
        typeInElement(typeNewPassword, newPassword);
    }

    public void typeConfirmNewPassword(String confirmPassword){
        typeInElement(typeConfirmPassword, confirmPassword);
    }

    public void clickSaveButton(){
        clickOnElement(saveNewPassword);
    }

    public void clickChangePassword(){
        clickOnElement(changePassword);
    }
    public void account(String currentPassword, String newPassword, String confirmPassword){
        clickChangePassword();
        typeCurrentPassword(currentPassword);
        typeNewPassword(newPassword);
        typeConfirmNewPassword(confirmPassword);
        clickSaveButton();
    }

}

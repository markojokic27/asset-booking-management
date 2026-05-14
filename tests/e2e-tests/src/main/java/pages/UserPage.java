package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class UserPage extends CommonMethods {

    public UserPage(){
        super();
    }

    // Add user
    public By userOpenModal  = By.cssSelector("[data-testid='add-user-button']");
    public By userModalClose = By.cssSelector("[data-testid='close-button']");
    public By userRoleField = By.cssSelector("[data-testid='user-role']");
    public By userStatusField = By.cssSelector("[data-testid='user-status']");
    public By userNameField = By.cssSelector("[data-testid='user-name']");
    public By userSurnameField = By.cssSelector("[data-testid='user-surname']");
    public By userUsernameField = By.cssSelector("[data-testid='user-username']");
    public By userPasswordField = By.cssSelector("[data-testid='user-password']");
    public By userIdField = By.cssSelector("[data-testid='user-department-id']");
    public By userEmailField = By.cssSelector("[data-testid='user-email']");
    public By userManagerEmailField = By.cssSelector("[data-testid='user-manager-email']");
    public By userNotesField = By.cssSelector("[data-testid='user-note']");
    public By addUserButton = By.cssSelector("[data-testid='create-user-button']");

    public void userOpenModal() {
        clickOnElement(userOpenModal);
    }

    public void userModalClose() {
        clickOnElement(userModalClose);
    }
    public void clickUserButton(){
        clickOnElement(addUserButton);
    }

    public void selectRole(String role){
        selectByVisibleText(userRoleField, role);
    }

    public void selectStatus(String status){
        selectByVisibleText(userStatusField, status);
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

    public void typeId(String id){
        typeInElement(userIdField, id);
    }

    public void typeEmail(String email){
        typeInElement(userEmailField, email);
    }

    public void typeManagerEmail(String managerEmail){
        typeInElement(userManagerEmailField, managerEmail);
    }

    public void typeNotes(String notes){
        typeInElement(userNotesField, notes);
    }

    public void user(String role, String status, String name, String surname, String username, String password, String id, String email, String managerEmail, String notes){
        selectRole(role);
        selectStatus(status);
        typeName(name);
        typeSurname(surname);
        typeUsername(username);
        typePassword(password);
        typeId(id);
        typeEmail(email);
        typeManagerEmail(managerEmail);
        typeNotes(notes);
        clickUserButton();

    }



}

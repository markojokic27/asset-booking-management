package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class UserPage extends CommonMethods {

    public UserPage(){
        super();
    }

    // Locators - Add user
    public By userOpenModal = By.cssSelector("[data-testid='add-user-button']");
    public By userModalClose = By.cssSelector("[data-testid='close-button']");
    public By userRoleField = By.cssSelector("[data-testid='user-role']");
    public By userStatusField = By.cssSelector("[data-testid='user-status']");
    public By userNameField = By.cssSelector("[data-testid='user-name']");
    public By userSurnameField = By.cssSelector("[data-testid='user-surname']");
    public By userUsernameField = By.cssSelector("[data-testid='user-username']");
    public By userPasswordField = By.cssSelector("[data-testid='user-password']");
    public By userEmailField = By.cssSelector("[data-testid='user-email']");
    public By userManagerEmailField = By.cssSelector("[data-testid='user-manager-email']");
    public By userNotesField = By.cssSelector("[data-testid='user-note']");
    public By addUserButton = By.cssSelector("[data-testid='create-user-button']");

    // Locators - Edit user
    public By userOpenEditModal = By.cssSelector("[data-testid='edit-user-button']");
    public By userEditCloseModal = By.cssSelector("[data-testid='user-edit'] [data-testid='edit-close-button']");
    public By userEditRoleField = By.cssSelector("[data-testid='user-edit'] [data-testid='user-role']");
    public By userEditStatusField = By.cssSelector("[data-testid='user-edit'] [data-testid='user-status']");
    public By userEditNameField = By.cssSelector("[data-testid='user-edit'] [data-testid='user-name']");
    public By userEditSurnameField = By.cssSelector("[data-testid='user-edit'] [data-testid='user-surname']");
    public By userEditEmailField = By.cssSelector("[data-testid='user-edit'] [data-testid='user-email']");
    public By userEditManagerEmailField = By.cssSelector("[data-testid='user-edit'] [data-testid='user-manager-email']");
    public By userEditNotesField = By.cssSelector("[data-testid='user-edit'] [data-testid='user-note']");
    public By saveUserButton = By.cssSelector("[data-testid='user-edit'] [data-testid='button-save']");

    // Locators - Delete user
    public By userOpenDeleteModal = By.cssSelector("[data-testid='delete-user-button']");
    public By userDeleteCancelButton = By.cssSelector("[data-testid='cancel-delete-button']");
    public By userDeleteConfirmButton = By.cssSelector("[data-testid='confirm-delete-button']");

    // Locators - View user
    public By userViewOpenModal = By.cssSelector("[data-testid='view-user-button']");
    public By userViewCloseModal = By.cssSelector("[data-testid='user-close-button']");

    // Locators - View bookings for user
    public By userBookingOpenModal = By.cssSelector("[data-testid='user-bookings-button']");
    public By userBookingCloseModal = By.cssSelector("[data-testid='user-booking-close-button']");

    // Locators - Search
    public By searchField = By.cssSelector("[data-testid='search-input']");
    public By filterRole = By.cssSelector("[data-testid='user-role-filter']");
    public By filterDepartment = By.cssSelector("[data-testid='user-department-filter']");
    public By clickShowDeleted = By.cssSelector("[data-testid='toggle-deleted']");




    // Add user

    public void userOpenModal() {
        clickOnElement(userOpenModal);
    }

    public void userModalClose() {
        clickOnElement(userModalClose);
    }

    public void clickUserButton() {
        clickOnElement(addUserButton);
    }

    public void selectRole(String role) {
        selectByVisibleText(userRoleField, role);
    }

    public void selectStatus(String status) {
        selectByVisibleText(userStatusField, status);
    }

    public void typeName(String name) {
        typeInElement(userNameField, name);
    }

    public void typeSurname(String surname) {
        typeInElement(userSurnameField, surname);
    }

    public void typeUsername(String username) {
        typeInElement(userUsernameField, username);
    }

    public void typePassword(String password) {
        typeInElement(userPasswordField, password);
    }

    public void typeEmail(String email) {
        typeInElement(userEmailField, email);
    }

    public void typeManagerEmail(String managerEmail) {
        typeInElement(userManagerEmailField, managerEmail);
    }

    public void typeNotes(String notes) {
        typeInElement(userNotesField, notes);
    }

    public void user(String role, String status, String name, String surname, String username, String password, String email, String managerEmail, String notes) {
        selectRole(role);
        selectStatus(status);
        typeName(name);
        typeSurname(surname);
        typeUsername(username);
        typePassword(password);
        typeEmail(email);
        typeManagerEmail(managerEmail);
        typeNotes(notes);
        clickUserButton();
    }


    // View user

    public void userViewOpenModal() {
        clickOnElement(userViewOpenModal);
    }

    public void userViewCloseModal() {
        clickOnElement(userViewCloseModal);
    }


    // View bookings for user

    public void userBookingOpenModal() {
        clickOnElement(userBookingOpenModal);
    }

    public void userBookingCloseModal() {
        clickOnElement(userBookingCloseModal);
    }


    // Edit user
    public void userOpenEditModalByUsername(String username) {
        searchUsers(username);
        clickOnElement(userOpenEditModal);
    }

    public void userEditCloseModal() {
        clickOnElement(userEditCloseModal);
    }

    public void selectEditRole(String role) {
        selectByVisibleText(userEditRoleField, role);
    }

    public void selectEditStatus(String status) {
        selectByVisibleText(userEditStatusField, status);
    }

    public void typeEditName(String name) {
        typeInElement(userEditNameField, name);
    }

    public void typeEditSurname(String surname) {

        typeInElement(userEditSurnameField, surname);
    }

    public void typeEditEmail(String email) {
        typeInElement(userEditEmailField, email);
    }

    public void typeEditManagerEmail(String managerEmail) {
        typeInElement(userEditManagerEmailField, managerEmail);
    }

    public void typeEditNotes(String notes) {
        typeInElement(userEditNotesField, notes);
    }

    public void clickSaveUserButton() {
        clickOnElement(saveUserButton);
    }

    public void editUser(String role, String status, String name, String surname, String email, String managerEmail, String notes) {
        selectEditRole(role);
        selectEditStatus(status);
        typeEditName(name);
        typeEditSurname(surname);
        typeEditEmail(email);
        typeEditManagerEmail(managerEmail);
        typeEditNotes(notes);
        clickSaveUserButton();
    }


    // Delete user

    public void userOpenDeleteModalByUsername(String username) {
        searchUsers(username);
        clickOnElement(userOpenDeleteModal);
        typeInElement(searchField, "");
    }

    public void userDeleteCancel() {
        clickOnElement(userDeleteCancelButton);
    }

    public void userDeleteConfirm() {
        clickOnElement(userDeleteConfirmButton);
    }

    // Search users

    public void searchUsers(String users) {
        typeInElement(searchField, users);
    }
    public void selectUserRole(String role){selectByVisibleText(filterRole, role);}
    public void selectUserDepartment(String department){selectByVisibleText(filterDepartment, department);}
    public void clickShowDelete(){
        clickOnElement(clickShowDeleted);
    }




}
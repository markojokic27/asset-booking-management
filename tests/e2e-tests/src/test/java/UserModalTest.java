import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.junit.jupiter.api.Assertions.*;

public class UserModalTest extends BaseTest {

    private void navigateToUsers() {
        WebElement usersLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("nav a[href='/users']")
                )
        );
        usersLink.click();
        wait.until(ExpectedConditions.urlContains("/users"));
    }

    private void openUserViewModal() {
        login();
        navigateToUsers();
        WebElement viewButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[aria-label='View user']")
                )
        );
        viewButton.click();
    }

    private void openUserEditModal() {
        login();
        navigateToUsers();
        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[aria-label='Edit user']")
                )
        );
        editButton.click();
    }

    // UserModal (view)

    @Test
    void userViewModalDisplaysCorrectData() {
        openUserViewModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='User details']"))
        );
        assertFalse(driver.findElement(
                By.xpath("//p[text()='Name']/following-sibling::p")
        ).getText().isBlank());
        assertFalse(driver.findElement(
                By.xpath("//p[text()='Email']/following-sibling::p")
        ).getText().isBlank());
    }

    @Test
    void userViewModalClosesOnCloseButton() {
        openUserViewModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='User details']"))
        );
        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='User details'] button[aria-label='Close']")
        ).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='User details']"))
        );
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='User details']")
        ).isEmpty());
    }

    // UserEditModal

    @Test
    void userEditModalClosesOnCloseButton() {
        openUserEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']"))
        );
        driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user'] button[aria-label='Close']")
        ).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']"))
        );
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isEmpty());
    }


    @Test
    void userEditModalSavesValidChanges() {
        openUserEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']"))
        );
        WebElement nameInput = driver.findElement(By.cssSelector("[name='name']"));
        nameInput.clear();
        nameInput.sendKeys("Ane");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']"))
        );
        assertTrue(driver.findElements(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isEmpty());
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//table//tbody//tr//td[contains(text(),'Anić Ane')]")
        ));
        assertTrue(driver.findElement(
                By.xpath("//table//tbody//tr//td[contains(text(),'Anić Ane')]")
        ).isDisplayed());
    }

    @Test
    void userEditModalShowsErrorForEmptyFirstName() {
        openUserEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']"))
        );
        WebElement nameInput = driver.findElement(By.cssSelector("[name='name']"));
        nameInput.clear();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isDisplayed());
    }

    @Test
    void userEditModalShowsErrorForEmptyLastName() {
        openUserEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']"))
        );
        WebElement surnameInput = driver.findElement(By.cssSelector("[name='surname']"));
        surnameInput.clear();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isDisplayed());
    }

    @Test
    void userEditModalShowsErrorForEmptyEmail() {
        openUserEditModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[role='dialog'][aria-label='Edit user']"))
        );
        WebElement emailInput = driver.findElement(By.cssSelector("[name='email']"));
        emailInput.clear();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.findElement(
                By.cssSelector("[role='dialog'][aria-label='Edit user']")
        ).isDisplayed());
    }
}
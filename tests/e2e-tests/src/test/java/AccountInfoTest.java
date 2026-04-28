import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AccountInfoTest extends BaseTest {

    private void navigateToAccountInfo() {
        login();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/account-info']")
        )).click();

        wait.until(ExpectedConditions.urlContains("/account-info"));
    }

    @Test
    void accountInfoPageDisplaysUserData() {
        navigateToAccountInfo();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='account-heading']")
        ));

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='account-fullname']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='account-email']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='account-role']")
        ).getText().isBlank());

        assertFalse(driver.findElement(
                By.cssSelector("[data-testid='account-status']")
        ).getText().isBlank());
    }
}
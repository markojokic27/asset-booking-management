import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AccountInfoTest extends BaseTest {

    private static final String NAV_LINK = "http://localhost:5173/account-info";
    private static final By HEADING = By.cssSelector("[data-testid='account-heading']");
    private static final By FULL_NAME = By.cssSelector("[data-testid='account-fullname']");
    private static final By EMAIL = By.cssSelector("[data-testid='account-email']");
    private static final By ROLE = By.cssSelector("[data-testid='account-role']");
    private static final By STATUS = By.cssSelector("[data-testid='account-status']");

    private void navigateToAccountInfo() {
        login();
        driver.get(NAV_LINK);
        wait.until(ExpectedConditions.visibilityOfElementLocated(HEADING));
    }

    private void assertNotBlank(By locator) {
        String text = driver.findElement(locator).getText();
        assertNotNull(text);
        assertFalse(text.isBlank());
    }

    @Test
    void accountInfoPageDisplaysUserData() {
        navigateToAccountInfo();
        wait.until(ExpectedConditions.visibilityOfElementLocated(HEADING));
        assertNotBlank(FULL_NAME);
        assertNotBlank(EMAIL);
        assertNotBlank(ROLE);
        assertNotBlank(STATUS);
    }
}
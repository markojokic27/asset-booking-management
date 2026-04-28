import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest extends BaseTest {

    private void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href='/login']")
        )).click();

        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    void logoutNavigatesToLoginPage() {
        login();
        logout();

        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

}
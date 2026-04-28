import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeToggleTest extends BaseTest {

    private void openApp() {
        login();
        wait.until(ExpectedConditions.urlContains("/"));
    }

    private WebElement getThemeToggle() {
        return wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='theme-toggle']")
                )
        );
    }

    private String htmlClass() {
        return driver.findElement(By.tagName("html")).getAttribute("class");
    }

    
    @Test
    void themeToggleChangesThemeOnClick() {
        openApp();

        WebElement toggle = getThemeToggle();

        String beforeClass = htmlClass();
        String beforePressed = toggle.getAttribute("aria-pressed");

        toggle.click();

        wait.until(driver ->
                !htmlClass().equals(beforeClass)
        );

        String afterClass = htmlClass();
        String afterPressed = getThemeToggle().getAttribute("aria-pressed");

        assertNotEquals(beforeClass, afterClass);
        assertNotEquals(beforePressed, afterPressed);
    }

}
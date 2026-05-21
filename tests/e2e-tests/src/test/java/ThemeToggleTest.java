import baselogin.BaseLogin;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;


public class ThemeToggleTest extends BaseLogin {

    @Test
    void themeToggleChangesTheme() {
        String beforePressed = getDriver().findElement(themeTogglePage.themeToggleButton).getAttribute("aria-pressed");
        themeTogglePage.clickThemeToggle();
        String afterPressed = getDriver().findElement(themeTogglePage.themeToggleButton).getAttribute("aria-pressed");
        assertNotEquals(beforePressed, afterPressed);
    }

}
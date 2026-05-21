package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class ThemeTogglePage extends CommonMethods {

    public ThemeTogglePage() {
        super();
    }

    public By themeToggleButton = By.cssSelector("[data-testid='theme-toggle']");
    public void clickThemeToggle() {
        clickOnElement(themeToggleButton);
    }
}
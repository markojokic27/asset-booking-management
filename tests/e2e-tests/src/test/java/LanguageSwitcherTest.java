import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageSwitcherTest extends BaseTest {

    private void openApp() {
        login();
        wait.until(ExpectedConditions.urlContains("/"));
    }

    private void openLanguageMenu() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='language-switcher']")
                )
        );

        button.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@role='menuitem'][.//span[text()='English']]")
        ));
    }

    private void selectLanguage(String label) {
        WebElement item = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@role='menuitem'][.//span[text()='" + label + "']]")
                )
        );

        item.click();
    }

    private String getLanguageFromLocalStorage() {
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('language');");
    }

    @Test
    void languageSwitcherOpensMenuAndShowsAllLanguages() {
        openApp();
        openLanguageMenu();

        assertTrue(driver.findElement(
                By.xpath("//*[@role='menuitem'][.//span[text()='Hrvatski']]")
        ).isDisplayed());

        assertTrue(driver.findElement(
                By.xpath("//*[@role='menuitem'][.//span[text()='English']]")
        ).isDisplayed());

        assertTrue(driver.findElement(
                By.xpath("//*[@role='menuitem'][.//span[text()='Deutsch']]")
        ).isDisplayed());
    }

    
    @Test
    void languageSwitcherSelectsEnglish() {
        openApp();
        openLanguageMenu();

        selectLanguage("English");

        wait.until(driver -> "en".equals(getLanguageFromLocalStorage()));

        assertEquals("en", getLanguageFromLocalStorage());
    }

    @Test
    void languageSwitcherSelectsGerman() {
        openApp();
        openLanguageMenu();

        selectLanguage("Deutsch");

        wait.until(driver -> "de".equals(getLanguageFromLocalStorage()));

        assertEquals("de", getLanguageFromLocalStorage());
    }

    @Test
    void languageSwitcherSelectsCroatian() {
        openApp();
        openLanguageMenu();

        selectLanguage("Hrvatski");

        wait.until(driver -> "hr".equals(getLanguageFromLocalStorage()));

        assertEquals("hr", getLanguageFromLocalStorage());
    }
}
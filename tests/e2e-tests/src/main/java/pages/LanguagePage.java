package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class LanguagePage extends CommonMethods {

    public LanguagePage() {
        super();
    }

    public By languageSwitcherButton = By.cssSelector("[data-testid='language-switcher']");
    public By languageOptionEn = By.cssSelector("[data-testid='language-option-en']");
    public By languageOptionDe = By.cssSelector("[data-testid='language-option-de']");
    public By languageOptionHr = By.cssSelector("[data-testid='language-option-hr']");

    public void openLanguageMenu() {
        clickOnElement(languageSwitcherButton);
    }

    public void selectLanguage(By option) {
        clickOnElement(option);
    }
}
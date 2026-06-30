package commonmethods;

import config.ConfigFromFile;
import constants.CommonConstants;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
public class CommonMethods  {

    protected static WebDriver driver;
    protected static WebDriverWait wait;

    protected CommonMethods() {}

    public WebDriver getDriver() {
        return driver;
    }


    // Browser setup

    public static boolean openBrowser() {
        try {
            createDriver();
            driver.get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.LOGIN_URL_EXTENSION);
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return true;
        } catch (Exception e) {
            log.error("openBrowser failed", e);
            return false;
        }
    }

    public static void closeBrowser() {
        driver.quit();
    }

    public static void createDriver() {
        try {
            String browser = ConfigFromFile.getParameters().get(CommonConstants.BROWSER);
            if (browser.equalsIgnoreCase(CommonConstants.FIREFOX)) {
                FirefoxOptions options = new FirefoxOptions();
                options.setBinary("/snap/firefox/current/usr/lib/firefox/firefox");
                driver = new FirefoxDriver(options);
            } else if (browser.equalsIgnoreCase(CommonConstants.CHROME)) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                driver = new ChromeDriver(options);
            }
        } catch (Exception e) {
            log.error("createDriver failed", e);
        }
    }


    // Element helpers

    private static WebElement getElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void clickOnElement(By locator) {
        try {
            By overlay = By.cssSelector("button[aria-label='Close dialog']");
            try {
                WebElement overlayEl = driver.findElement(overlay);
                if (overlayEl.isDisplayed()) {
                    overlayEl.click();
                }
            } catch (Exception ignored) {}
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            log.error("clickOnElement failed", e);
        }
    }

    public static void typeInElement(By locator, String text) {
        try {
            WebElement element = getElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            element.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            if (text != null && !text.isEmpty()) {
                element.sendKeys(text);
            }
        } catch (Exception e) {
            log.error("typeInElement failed", e);
        }
    }

    public static void selectByVisibleText(By locator, String text) {
        try {
            new Select(getElement(locator)).selectByVisibleText(text);
        } catch (Exception e) {
            log.error("selectByVisibleText failed", e);
        }
    }

    public static void inputDate(By locator, String date) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript(
                    "var s = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                            "s.call(arguments[0], arguments[1]);" +
                            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                            "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                    element, date
            );
        } catch (Exception e) {
            log.error("inputDate failed", e);
        }
    }

    public static void jsClick(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            log.error("jsClick failed", e);
        }
    }

    public static void scrollToElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } catch (Exception e) {
            log.error("scrollToElement failed", e);
        }
    }


    // Visibility checks

    public static boolean isElementVisible(By locator) {
        try {
            getElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean elementHasClass(By locator, String cssClass) {
        try {
            String classes = driver.findElement(locator).getAttribute("class");
            return classes != null && classes.contains(cssClass);
        } catch (Exception e) {
            return false;
        }
    }


    // URL & navigation

    public static boolean waitForUrlContains(String extension) {
        try {
            return wait.until(ExpectedConditions.urlContains(extension));
        } catch (Exception e) {
            log.error("waitForUrlContains failed", e);
            return false;
        }
    }

    public static String getUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            log.error("getUrl failed", e);
            return "";
        }
    }
}
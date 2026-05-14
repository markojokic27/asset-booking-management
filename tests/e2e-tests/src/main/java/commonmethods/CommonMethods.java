package commonmethods;

import config.ConfigFromFile;
import constants.CommonConstants;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
public class CommonMethods {

    @Getter
    private static WebDriver driver;
    private static WebDriverWait wait;

    protected CommonMethods() {
    }

    public static boolean openBrowser() {
        try {
            createDriver();
            getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.LOGIN_URL_EXTENSION);
            getDriver().manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return true;
        } catch (Exception e) {
            log.error("openBrowser", e);
        }
        return false;
    }

    public static void closeBrowser() {
        getDriver().quit();
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
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(options);
            }
        } catch (Exception e) {
            log.error("createDriver", e);
        }
    }

    public static void clickOnElement(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            driver.findElement(locator).click();
        } catch (Exception e) {
            log.error("click on element failed", e);
        }
    }

    public static void typeInElement(By locator, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            driver.findElement(locator).sendKeys(text);
        } catch (Exception e) {
            log.error("type in element failed", e);
        }
    }

    public static void selectByVisibleText(By locator, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            Select select = new Select(driver.findElement(locator));
            select.selectByVisibleText(text);

        } catch (Exception e) {
            log.error("select by visible text failed", e);
        }
    }

    public static boolean waitForUrlContains(String extension) {
        try {
            return wait.until(ExpectedConditions.urlContains(extension));
        } catch (Exception e) {
            log.error("waitForUrl failed", e);
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
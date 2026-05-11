import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;
import java.util.function.Supplier;

public final class BrowserFactory {

    private BrowserFactory() {
    }

    private static final Map<String, Supplier<WebDriver>> BROWSERS = Map.of(
            "chrome", BrowserFactory::createChrome,
            "firefox", BrowserFactory::createFirefox
    );

    public static WebDriver create(String browserName) {

        Supplier<WebDriver> supplier =
                BROWSERS.get(browserName.toLowerCase());

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unsupported browser: " + browserName
            );
        }

        return supplier.get();
    }

    private static WebDriver createChrome() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox() {

        FirefoxOptions options = new FirefoxOptions();

        options.setBinary("/snap/firefox/current/usr/lib/firefox/firefox");

        options.addArguments("--headless=new");

        return new FirefoxDriver(options);
    }
}
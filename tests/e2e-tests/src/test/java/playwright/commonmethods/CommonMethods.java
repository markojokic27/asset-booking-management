package playwright.commonmethods;

import com.microsoft.playwright.Page;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonMethods {

    protected Page page;

    protected CommonMethods(Page page) {
        this.page = page;
    }

    public void clickOnElement(String locator) {
        try {
            page.locator(locator).click();
        } catch (Exception e) {
            log.error("click on element failed", e);
        }
    }

    public void typeInElement(String locator, String text) {
        try {
            page.locator(locator).fill(text != null ? text : "");
        } catch (Exception e) {
            log.error("type in element failed", e);
        }
    }

    public boolean waitForUrlContains(String extension) {
        try {
            page.waitForURL("**" + extension + "**");
            return true;
        } catch (Exception e) {
            log.error("waitForUrl failed", e);
            return false;
        }
    }
}
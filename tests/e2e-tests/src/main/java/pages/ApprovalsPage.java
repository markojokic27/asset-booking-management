package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class ApprovalsPage extends CommonMethods {

    public ApprovalsPage() {
        super();
    }

    // Locators
    public By modalApproveButton = By.cssSelector("[aria-modal='true'] [data-testid^='approve-booking-']");


    public void clickApproveInModal() {
        isElementVisible(modalApproveButton);
        clickOnElement(modalApproveButton);
    }
}
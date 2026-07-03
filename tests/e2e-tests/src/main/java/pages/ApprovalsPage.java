package pages;

import commonmethods.CommonMethods;
import org.openqa.selenium.By;

public class ApprovalsPage extends CommonMethods {

    public ApprovalsPage() {
        super();
    }

    public By tableApproveButton = By.cssSelector("[data-testid^='approve-booking-']");

    public void clickApproveInModal() {
        clickOnElement(tableApproveButton);
    }
}
package report;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ViewBookingsTest extends BaseLogin {

    @BeforeMethod
    public void setUpReportPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.REPORT_URL));
    }

    @Test
    public void viewBookingByStatus(){
        reportPage.viewBookingByStatus();
        assertTrue(waitForUrlContains(CommonConstants.REPORT_URL));
    }

    @Test
    public void viewBookingsByMonth() {
        reportPage.viewBookingByMonth();
        assertTrue(waitForUrlContains(CommonConstants.REPORT_URL));

    }

    @Test
    public void viewTopAssetsByNumberOfBookings(){
        reportPage.viewTopAssets();
        assertTrue(waitForUrlContains(CommonConstants.REPORT_URL));

    }

    @Test
    public void viewTopUsersByNumberOfBookings(){
        reportPage.viewTopUsers();
        assertTrue(waitForUrlContains(CommonConstants.REPORT_URL));
    }
}

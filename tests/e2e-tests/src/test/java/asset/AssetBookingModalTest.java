package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetBookingModalTest extends BaseLogin {

    @BeforeMethod
    void setUpAssetPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));

        assetPage.assetBookingOpenModal();
    }


    @Test
    public void assetBookingModalCloseOnCloseButton(){
        assetPage.assetBookingCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }
}

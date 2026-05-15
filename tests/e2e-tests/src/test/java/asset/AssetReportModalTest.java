package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;


public class AssetReportModalTest extends BaseLogin {

    @BeforeMethod
    void setUpAssetPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));

        assetPage.assetReportOpenModal();
    }


    @Test
    public void assetReportModalCloseOnCloseButton(){
        assetPage.assetReportCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }


}

package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class VIewDeletedAssetsTest extends BaseLogin {

    @Test
    public void viewDeletedAssets(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));
        assetPage.clickToggleDeletedAssets();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));

    }

}

package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetSearchTest extends BaseLogin {

    @Test
    public void searchAssets(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));
        assetPage.searchAssets(CommonConstants.SEARCH_ASSET);
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));

    }

}

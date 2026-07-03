package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetSearchTest extends BaseLogin {

    @BeforeMethod
    public void setUpAsset(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));

    }

    @Test
    public void searchAsset(){
        assetPage.searchAssets(CommonConstants.SEARCH_ASSET);
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));

    }

    @Test
    public void selectAssetStatus(){
        assetPage.selectAssetStatus(CommonConstants.VALID_STATUS);
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));

    }

}

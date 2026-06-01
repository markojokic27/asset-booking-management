package assetCategory;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetCategoryFilterTest extends BaseLogin {

    @Test
    public void filterAssetsByCategoryOnAssetPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));
        assetPage.clickLaptopCategory();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }



}
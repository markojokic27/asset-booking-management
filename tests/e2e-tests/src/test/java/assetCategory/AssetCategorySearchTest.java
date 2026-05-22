package assetCategory;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetCategorySearchTest extends BaseLogin {

    @Test
    public void searchAssetsCategory(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.CATEGORY_URL));
        assetCategoryPage.searchCategoryAssets(CommonConstants.SEARCH_CATEGORY);
        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));

    }
}

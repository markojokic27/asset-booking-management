package assetCategory;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static org.testng.AssertJUnit.assertTrue;

public class AssetCategoryViewModalTest extends BaseLogin {

    @BeforeMethod
    void setUpAssetCategoryPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.CATEGORY_URL));

        assetCategoryPage.assetCategoryViewOpenModal();
    }


    @Test
    public void assetCategoryViewModalCloseOnCloseButton(){
        assetCategoryPage.assetCategoryViewCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }
}

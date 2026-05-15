package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetDeleteModalTest extends BaseLogin {

    @BeforeMethod
    void setUpAssetPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));

        assetPage.assetDeleteOpenModal();
    }

    @Test
    public void assetDeleteCancel(){
        assetPage.cancelDeleteButton();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));

    }

    @Test
    public void assetDeleteConfirm (){
        assetPage.confirmDeleteButton();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }


}

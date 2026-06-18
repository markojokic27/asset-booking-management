package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetSmokeTest extends BaseLogin {


    @BeforeMethod
    void setUpAssetPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));

    }

    @Test
    void successAddNewAsset(){
        assetPage.assetOpenModal();
            assetPage.asset(
                    CommonConstants.VALID_STATUS,
                    CommonConstants.CATEGORY,
                    CommonConstants.VALID_ASSET_NAME,
                    CommonConstants.VALID_LOCATION,
                    CommonConstants.VALID_DESCRIPTION

            );

            assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
        }



}

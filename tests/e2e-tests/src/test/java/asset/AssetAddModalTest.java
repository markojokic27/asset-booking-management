package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;


public class AssetAddModalTest extends BaseLogin {

    @BeforeMethod
    void setUpAssetPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));

        assetPage.assetOpenModal();
    }

    @Test
    public void assetAddModalClosesOnCloseButton(){
        assetPage.assetCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

    // Add asset with valid data

    @Test
    void assetAddModalSavesValidAsset() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                CommonConstants.VALID_LOCATION,
                CommonConstants.VALID_DESCRIPTION

        );

        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

    // Add asset with empty fields

    @Test
    void assetAddModalWithEmptyName() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                "",
                CommonConstants.VALID_LOCATION,
                CommonConstants.VALID_DESCRIPTION
        );

               assertTrue(isElementVisible(assetPage.assetModal));
    }

    @Test
    void assetAddModalWithEmptyCategory(){
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.EMPTY_CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                CommonConstants.VALID_LOCATION,
                CommonConstants.VALID_DESCRIPTION
        );
        assertTrue(isElementVisible(assetPage.assetModal));
    }

    @Test
    void assetAddModalWithEmptyLocation() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                "",
                CommonConstants.VALID_DESCRIPTION
        );

         assertTrue(isElementVisible(assetPage.assetModal));
    }

    @Test
    void assetAddModalWithEmptyDescription() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                CommonConstants.VALID_LOCATION,
                ""
        );

        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

    @Test
    void assetAddModalWithAllEmptyFields() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.EMPTY_CATEGORY,
                "",
                "",
                ""
        );

         assertTrue(isElementVisible(assetPage.assetModal));
    }

    // Create asset with long fields

    @Test
    void assetAddWithLongName() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.LONG_ASSET_NAME,
                CommonConstants.VALID_LOCATION,
                CommonConstants.VALID_DESCRIPTION
        );

         assertTrue(isElementVisible(assetPage.assetModal));
    }

    @Test
    void assetAddWithLongLocation() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                CommonConstants.LONG_ASSET_LOCATION,
                CommonConstants.VALID_DESCRIPTION
        );

         assertTrue(isElementVisible(assetPage.assetModal));
    }

    @Test
    void assetAddWithLongDescription() {
        assetPage.asset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                CommonConstants.VALID_LOCATION,
                CommonConstants.LONG_DESCRIPTION
        );

        assertTrue(isElementVisible(assetPage.assetModal));
    }

    // Change status

    @Test
    void assetChangeStatus() {
        assetPage.asset(
                CommonConstants.CHANGE_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                CommonConstants.VALID_LOCATION,
                CommonConstants.VALID_DESCRIPTION
        );

        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

}

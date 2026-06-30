package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetEditModalTest extends BaseLogin {

    @BeforeMethod
    void setUpAssetPage(){
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.ASSETS_URL));

        assetPage.assetEditOpenModal();
    }

    @Test
    public void assetEditModalCloseOnCloseButton(){
        assetPage.assetEditCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

    // Edit asset with valid data

    @Test
    void assetEditModalSavesValidAsset() {
        assetPage.editAsset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                CommonConstants.VALID_ASSET_NAME,
                CommonConstants.VALID_LOCATION,
                CommonConstants.VALID_DESCRIPTION
        );

        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

    // Edit asset with empty fields

    @Test
    void assetEditModalWithEmptyName() {
        assetPage.typeName("");
        assetPage.clickEditButton();

        assertTrue(isElementVisible(assetPage.assetEditModal));
    }

    @Test
    void assetEditModalWithEmptyLocation() {
        assetPage.typeLocation("");
        assetPage.clickEditButton();

        assertTrue(isElementVisible(assetPage.assetEditModal));
    }

    @Test
    void assetEditModalWithEmptyDescription() {
        assetPage.typeDescription("");
        assetPage.clickEditButton();

        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

    @Test
    void assetEditModalWithAllEmptyFields() {
        assetPage.editAsset(
                CommonConstants.VALID_STATUS,
                CommonConstants.CATEGORY,
                "",
                "",
                ""
        );

        assertTrue(isElementVisible(assetPage.assetEditModal));
    }

    // Create asset with long fields

    @Test
    void assetEditWithLongName() {
        assetPage.typeName(CommonConstants.LONG_ASSET_NAME);
        assetPage.clickEditButton();

        assertTrue(isElementVisible(assetPage.assetEditModal));
    }

    @Test
    void assetEditWithLongLocation() {
        assetPage.typeLocation(CommonConstants.LONG_ASSET_LOCATION);
        assetPage.clickEditButton();


        assertTrue(isElementVisible(assetPage.assetEditModal));
    }

    @Test
    void assetEditWithLongDescription() {
        assetPage.typeDescription(CommonConstants.LONG_DESCRIPTION);
        assetPage.clickEditButton();


        assertTrue(isElementVisible(assetPage.assetEditModal));
    }

    // Change status

    @Test
    void assetChangeStatus() {
        assetPage.selectStatus(CommonConstants.CHANGE_STATUS);
        assetPage.clickEditButton();


        assertTrue(waitForUrlContains(CommonConstants.ASSETS_URL));
    }

}

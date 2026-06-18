package smokeTest;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetCategorySmokeTest extends BaseLogin {

    @BeforeMethod
    public void setUpCategoryPage() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.CATEGORY_URL));
        assetCategoryPage.openCategoryModal();
    }

    // Add category with valid data

    @Test
    void addCategoryWithValidData() {
        assetCategoryPage.category(
                CommonConstants.VALID_CATEGORY_NAME,
                CommonConstants.VALID_CATEGORY_DESCRIPTION,
                CommonConstants.VALID_BOOKING_PERIOD
        );

        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }

    // Add category with approval

    @Test
    void addCategoryWitApproval() {
        assetCategoryPage.category(
                CommonConstants.VALID_CATEGORY_NAME,
                CommonConstants.VALID_CATEGORY_DESCRIPTION,
                CommonConstants.VALID_BOOKING_PERIOD
        );

        assetCategoryPage.clickCategoryApproval();
        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }


}

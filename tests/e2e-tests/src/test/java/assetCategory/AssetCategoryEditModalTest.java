package assetCategory;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AssetCategoryEditModalTest extends BaseLogin {

    @BeforeMethod
    public void setUpCategoryPage() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.CATEGORY_URL));
        assetCategoryPage.assetCategoryEditOpenModal();
    }

    // Open and close modal

    @Test
    void editCategoryModalClosesOnCloseButton() {
        assetCategoryPage.assetCategoryEditCloseModal();
        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }

    // Edit category with valid data

    @Test
    void editCategoryWithValidData() {
        assetCategoryPage.editCategory(
                CommonConstants.VALID_CATEGORY_NAME,
                CommonConstants.VALID_CATEGORY_DESCRIPTION,
                CommonConstants.VALID_BOOKING_PERIOD
        );

        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }

    // Edit category with empty fields

    @Test
    void editCategoryWithEmptyName() {
        assetCategoryPage.typeEditName("");
        assetCategoryPage.clickEditCategoryButton();

        assertTrue(isElementVisible(assetCategoryPage.assetCategoryEditModal));
    }

    @Test
    void editCategoryWithAllFieldsEmpty() {
        assetCategoryPage.typeEditName("");
        assetCategoryPage.typeEditDescription("");
        assetCategoryPage.clickEditCategoryButton();

        assertTrue(isElementVisible(assetCategoryPage.assetCategoryEditModal));
    }

    @Test
    void editCategoryWithoutDescription() {
        assetCategoryPage.typeEditDescription("");
        assetCategoryPage.clickEditCategoryButton();

        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }

    // Edit category with long fields

    @Test
    void editCategoryWithLongName() {
        assetCategoryPage.typeEditName(CommonConstants.LONG_CATEGORY_NAME);
        assetCategoryPage.clickEditCategoryButton();

        assertTrue(isElementVisible(assetCategoryPage.assetCategoryEditModal));
    }

    @Test
    void editCategoryWithLongDescription() {
        assetCategoryPage.typeEditDescription(CommonConstants.LONG_CATEGORY_DESCRIPTION);
        assetCategoryPage.clickEditCategoryButton();

        assertTrue(isElementVisible(assetCategoryPage.assetCategoryEditModal));
    }

    // Change booking period

    @Test
    void editCategoryWithChangeBookingPeriod() {
        assetCategoryPage.typeEditBookingPeriod(CommonConstants.CHANGE_BOOKING_PERIOD);

        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }

    // Edit category with approval

    @Test
    void editCategoryWitApproval() {
        assetCategoryPage.assetCategoryEditOpenModal();
        assetCategoryPage.typeEditName(CommonConstants.VALID_CATEGORY_NAME);
        assetCategoryPage.typeEditDescription(CommonConstants.VALID_CATEGORY_DESCRIPTION);
        assetCategoryPage.typeEditBookingPeriod(CommonConstants.VALID_BOOKING_PERIOD);
        assetCategoryPage.clickEditCategoryApproval();
        assetCategoryPage.clickEditCategoryButton();

        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }
}

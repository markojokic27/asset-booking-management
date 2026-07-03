package assetCategory;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class AddCategoryModalTest extends BaseLogin {

    @BeforeMethod
    public void setUpCategoryPage() {
        login();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + (CommonConstants.CATEGORY_URL));

        assetCategoryPage.openCategoryModal();
    }

    // Open and close modal

    @Test
    void addCategoryModalClosesOnCloseButton() {
        assetCategoryPage.closeCategoryModal();
        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
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

    // Add category with empty fields

    @Test
    void addCategoryWithEmptyName() {
        assetCategoryPage.category(
                "",
                CommonConstants.VALID_CATEGORY_DESCRIPTION,
                CommonConstants.VALID_BOOKING_PERIOD

        );

        assertTrue(isElementVisible(assetCategoryPage.categoryModal));
    }

    @Test
    void addCategoryWithAllFieldsEmpty() {
        assetCategoryPage.category(
                "",
                "",
                CommonConstants.VALID_BOOKING_PERIOD
        );

       assertTrue(isElementVisible(assetCategoryPage.categoryModal));
    }

    @Test
    void addCategoryWithoutDescription() {
        assetCategoryPage.category(
                CommonConstants.VALID_CATEGORY_NAME,
                "",
                CommonConstants.VALID_BOOKING_PERIOD
        );

        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));

    }

    // Add category with long fields

    @Test
    void addCategoryWithLongName() {
        assetCategoryPage.category(
                CommonConstants.LONG_CATEGORY_NAME,
                CommonConstants.VALID_CATEGORY_DESCRIPTION,
                CommonConstants.VALID_BOOKING_PERIOD
        );

       assertTrue(isElementVisible(assetCategoryPage.categoryModal));
    }

    @Test
    void addCategoryWithLongDescription() {
        assetCategoryPage.category(
                CommonConstants.VALID_CATEGORY_NAME,
                CommonConstants.LONG_CATEGORY_DESCRIPTION,
                CommonConstants.VALID_BOOKING_PERIOD
        );

       assertTrue(isElementVisible(assetCategoryPage.categoryModal));
    }

    // Change booking period

    @Test
    void addCategoryWithChangeBookingPeriod() {
        assetCategoryPage.category(
                CommonConstants.VALID_CATEGORY_NAME,
                CommonConstants.VALID_CATEGORY_DESCRIPTION,
                CommonConstants.CHANGE_BOOKING_PERIOD
        );

        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }

    // Add category with approval

    @Test
    void addCategoryWitApproval() {
        assetCategoryPage.openCategoryModal();
        assetCategoryPage.typeName(CommonConstants.CATEGORY_NAME);
        assetCategoryPage.typeDescription(CommonConstants.VALID_CATEGORY_DESCRIPTION);
        assetCategoryPage.typeBookingPeriod(CommonConstants.VALID_BOOKING_PERIOD);
        assetCategoryPage.clickCategoryApproval();
        assetCategoryPage.clickCategoryButton();
        assertTrue(waitForUrlContains(CommonConstants.CATEGORY_URL));
    }
}

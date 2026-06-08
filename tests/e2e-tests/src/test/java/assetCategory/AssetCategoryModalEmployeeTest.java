package assetCategory;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;

public class AssetCategoryModalEmployeeTest extends BaseLogin {

    @BeforeMethod
    void setUpAssetPage(){
        loginWithEmployee();
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.CATEGORY_URL);
    }

    @Test
    void employeeCannotSeeAddButton(){
        assertFalse(isElementVisible(assetCategoryPage.openCategoryModal));
    }

    @Test
    void employeeCannotSeeEditButton() {
        assertFalse(isElementVisible(assetCategoryPage.editCategoryButton));
    }

}

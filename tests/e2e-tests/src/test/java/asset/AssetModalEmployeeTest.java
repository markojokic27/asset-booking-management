package asset;

import baselogin.BaseLogin;
import config.ConfigFromFile;
import constants.CommonConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;

public class AssetModalEmployeeTest extends BaseLogin {

        @BeforeMethod
        void setUpAssetPage(){
            loginWithEmployee();
            getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.ASSETS_URL);
        }

        @Test
        void employeeCannotSeeAddButton(){
            assertFalse(isElementVisible(assetPage.assetOpenModal));
        }

        @Test
        void employeeCannotSeeDeleteButton(){
            assertFalse(isElementVisible(assetPage.asseDeleteOpenModal));
        }

        @Test
        void employeeCannotSeeEditButton() {
            assertFalse(isElementVisible(assetPage.assetEditOpenModal));
        }


}
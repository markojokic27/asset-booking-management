package factory;

import commonmethods.CommonMethods;
import pages.*;

public class PageAndHandlerFactory extends CommonMethods {

    public static LoginPage loginPage;
    public static RegisterPage registerPage;

    public static UserPage userPage;

    public static AssetPage assetPage;
    public static AssetCategoryPage assetCategoryPage;
    public static AccountPage accountPage;

    public static void setupPagesAndHandlers() {
        loginPage = new LoginPage();
        registerPage=new RegisterPage();
        userPage=new UserPage();
        assetPage=new AssetPage();
        assetCategoryPage=new AssetCategoryPage();
        accountPage = new AccountPage();
    }
}

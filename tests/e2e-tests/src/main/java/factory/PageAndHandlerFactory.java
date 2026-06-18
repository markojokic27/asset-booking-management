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
    public static LogoutPage logoutPage;
    public static LanguagePage languagePage;
    public static ThemeTogglePage themeTogglePage;
    public static BookingPage bookingPage;
    public static ReportPage reportPage;
    public static MyBookingsPage myBookingsPage;
    public static ApprovalsPage approvalsPage;

    public static void setupPagesAndHandlers() {
        loginPage = new LoginPage();
        registerPage=new RegisterPage();
        userPage=new UserPage();
        assetPage=new AssetPage();
        assetCategoryPage=new AssetCategoryPage();
        accountPage = new AccountPage();
        logoutPage = new LogoutPage();
        languagePage = new LanguagePage();
        themeTogglePage = new ThemeTogglePage();
        bookingPage = new BookingPage();
        reportPage = new ReportPage();
        myBookingsPage = new MyBookingsPage();
        approvalsPage = new ApprovalsPage();
    }
}

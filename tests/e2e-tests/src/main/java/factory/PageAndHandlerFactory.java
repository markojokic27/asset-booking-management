package factory;

import commonmethods.CommonMethods;
import pages.LoginPage;
import pages.RegisterPage;
import pages.UserPage;

public class PageAndHandlerFactory extends CommonMethods {

    public static LoginPage loginPage;
    public static RegisterPage registerPage;

    public static UserPage userPage;

    public static void setupPagesAndHandlers() {
        loginPage = new LoginPage();
        registerPage=new RegisterPage();
        userPage=new UserPage();
    }
}

package playwright.factory;

import com.microsoft.playwright.Page;
import playwright.commonmethods.CommonMethods;
import playwright.pages.*;

public class PageAndHandlerFactory extends CommonMethods {

    public LoginPage loginPage;
    public RegisterPage registerPage;

    public PageAndHandlerFactory(Page page) {
        super(page);
    }

    public void setupPagesAndHandlers() {
        loginPage = new LoginPage(page);
        registerPage = new RegisterPage(page);
    }
}
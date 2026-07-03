package baselogin;

import config.ConfigFromFile;
import constants.CommonConstants;
import factory.PageAndHandlerFactory;
import io.github.cdimascio.dotenv.Dotenv;
import org.testng.annotations.*;

import static org.testng.Assert.assertTrue;

public class BaseLogin extends PageAndHandlerFactory {

    @BeforeSuite
    public void resetDatabase() {
        System.out.println(">>> STARTING DATABASE RESET <<<");
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            String user = dotenv.get("DB_USER");
            String db = dotenv.get("DB_NAME");
            String password = dotenv.get("DB_PASSWORD");
            String dumpFile = dotenv.get("DB_DUMP_FILE", "initial_state.sql");


            ProcessBuilder pb = new ProcessBuilder("docker", "exec", "-i", "postgres-db",
                    "psql", "-q", "-U", user, "-d", db);
            pb.redirectInput(ProcessBuilder.Redirect.from(new java.io.File(dumpFile)));
            pb.environment().put("PGPASSWORD", password);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            System.out.println("Resetting database, please wait...");
            Process process = pb.start();
            boolean finished = process.waitFor(30, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                System.err.println("Database reset timed out!");
            } else {
                System.out.println("Database reset done!");
            }
        } catch (Exception e) {
            System.err.println("Database reset failed: " + e.getMessage());
        }
    }

    @BeforeClass
    public void setUpBeforeTestClass() {
        assertTrue(openBrowser());
        setupPagesAndHandlers();
    }

    @BeforeMethod
    public void setUpBeforeEachTest() {
        getDriver().get(ConfigFromFile.getParameters().get(CommonConstants.BASE_URL) + CommonConstants.LOGIN_URL_EXTENSION);
    }

    @AfterClass
    public void tearDownAfterTestClass() {
        closeBrowser();
    }

    protected void login() {
        loginPage.typeUsername(CommonConstants.ADMIN_USERNAME);
        loginPage.typePassword(CommonConstants.ADMIN_PASS);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    protected void loginWithEmployee() {
        loginPage.typeUsername(CommonConstants.EMPLOYEE_USERNAME);
        loginPage.typePassword(CommonConstants.PASSWORD);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    protected void loginWithManager() {
        loginPage.typeUsername(CommonConstants.MANAGER_USERNAME);
        loginPage.typePassword(CommonConstants.MANAGER_PASS);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }

    protected void loginWithAndela() {
        loginPage.typeUsername(CommonConstants.USERNAME);
        loginPage.typePassword(CommonConstants.PASS);
        loginPage.clickLoginButton();
        assertTrue(waitForUrlContains(CommonConstants.BOOKINGS_URL_EXTENSION));
    }
}
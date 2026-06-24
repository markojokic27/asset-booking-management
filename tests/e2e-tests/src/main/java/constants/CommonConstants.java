package constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommonConstants {

    public static final String BASE_URL = "BASE_URL";
    public static final String BOOKINGS_URL_EXTENSION = "/bookings";
    public static final String LOGIN_URL_EXTENSION = "/login";
    public static final String REGISTER_URL_EXTENSION = "/register";
    public static final String USERS_URL = "/users";

    public static final String ASSETS_URL = "/assets";
    public static final String CATEGORY_URL = "/categories";
    public static final String ACCOUNT_INFO = "/account-info";
    public static final String REPORT_URL = "/report";
    public static final String MY_BOOKING_URL = "/my-bookings";

    public static final String APPROVALS = "/approvals";

    // Login
    public static final String ADMIN_USERNAME = "user_admin";
    public static final String ADMIN_PASS = "admin123";
    public static final String WRONG_USERNAME = "user_admin!";
    public static final String WRONG_PASSWORD = "pass.1234";
    public static final String EMPLOYEE_USERNAME = "user_employee";
    public static final String MANAGER_USERNAME = "mBanovic";
    public static final String MANAGER_PASS = "mladen123";
    public static final String USERNAME = "aMustapic";
    public static final String PASS = "andela1234";

    // Register and user
    public static final String VALID_NAME = "Ivan";
    public static final String VALID_USERNAME = "ivanivic";
    public static final String VALID_SURNAME = "Ivic";
    public static final String VALID_PASSWORD = "password.123";
    public static final String INVALID_NAME = "Ivan!";
    public static final String INVALID_USERNAME = "ivanivic?";
    public static final String INVALID_SURNAME = "Ivic!";
    public static final String SHORT_PASSWORD = "pas123";
    public static final String LONG_PASSWORD = "p".repeat(51);
    public static final String SHORT_NAME = "i";
    public static final String LONG_NAME = "i".repeat(101);
    public static final String SHORT_SURNAME = "i";
    public static final String LONG_SURNAME = "i".repeat(101);
    public static final String SHORT_USERNAME = "i";
    public static final String LONG_USERNAME = "i".repeat(51);

    public static final String VALID_EMAIL = "ivanivic@example.com";
    public static final String VALID_MANAGER_EMAIL = "manager@example.com";
    public static final String VALID_NOTES = "This is a dummy Ivan account";
    public static final String INVALID_EMAIL = "ivanivic";
    public static final String INVALID_MANAGER_EMAIL = "manager";
    public static final String LONG_EMAIL = "i".repeat(255);
    public static final String LONG_MANAGER_EMAIL = "i".repeat(255);
    public static final String LONG_NOTES = "i".repeat(1001);
    public static final String VALID_ROLE = "ADMIN";
    public static final String VALID_STATUS = "Active";
    public static final String VALID_ID = "1";

    // User and asset
    public static final String CHANGE_STATUS = "Inactive";
    public static final String EDIT_USER_USERNAME = "john.doe";
    public static final String DELETE_USER_USERNAME = "ivanivic";

    // Asset
    public static final String VALID_ASSET_NAME = "Parking Spot 16";
    public static final String VALID_LOCATION = "Floor plan 2";
    public static final String VALID_DESCRIPTION = "VIP parking";
    public static final String LONG_ASSET_NAME = "t".repeat(101);
    public static final String LONG_ASSET_LOCATION = "r".repeat(256);
    public static final String LONG_DESCRIPTION = "t".repeat(256);
    public static final String CATEGORY = "Parking";
    public static final String EMPTY_CATEGORY = "Select category";

    // Asset category
    public static final String VALID_CATEGORY_NAME = "Room";
    public static final String VALID_CATEGORY_DESCRIPTION = "All company rooms ";
    public static final String VALID_BOOKING_PERIOD = "Day";
    public static final String LONG_CATEGORY_NAME = "t".repeat(101);
    public static final String LONG_CATEGORY_DESCRIPTION = "t".repeat(256);
    public static final String CHANGE_BOOKING_PERIOD = "Hour";

    // Account info
    public static final String PASSWORD = "employee123";
    public static final String NEW_PASSWORD = "employee1234";

    // Search assets
    public static final String SEARCH_ASSET = "mac";

    // Search users
    public static final String SEARCH_USERS = "doe";

    // Search category
    public static final String SEARCH_CATEGORY = "laptop";

    // Booking
    public static final String BOOKED_ASSET_ID = "1";

    public static String getFutureDateFrom() {
        return LocalDate.now().plusDays(30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getFutureDateTo() {
        return LocalDate.now().plusDays(41)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // Parking
    public static final String FLOOR_LEVEL_MINUS_1 = "-1";
    public static final String FLOOR_LEVEL_MINUS_2 = "-2";
    public static String getParkingTestDate() {
        return LocalDate.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // My bookings list
    public static final String ASSET = "Desk A1";

    public static String getDateFrom() {
        return LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public static String getDateTo() {
        return LocalDate.now().plusDays(10)
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
    public static final String BROWSER = "BROWSER";

    // Drivers constants
    public static final String FIREFOX = "FirefoxWebDriver";
    public static final String CHROME = "ChromeWebDriver";
}
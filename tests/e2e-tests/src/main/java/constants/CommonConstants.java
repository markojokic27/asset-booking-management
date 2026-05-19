package constants;
public class CommonConstants{

    public static final String BASE_URL = "BASE_URL";
    public static final String BOOKINGS_URL_EXTENSION = "/bookings";
    public static final String LOGIN_URL_EXTENSION = "/login";
    public static final String REGISTER_URL_EXTENSION="/register";
    public static final String USERS_URL= "/users";
    public static final String ASSETS_URL= "/assets";
    public static final String CATEGORY_URL= "/categories";
    public static final String ACCOUNT_INFO="/account-info";

    //Login

    public static final String ADMIN_USERNAME = "user_admin";
    public static final String ADMIN_PASS = "admin123";
    public static final String WRONG_USERNAME="user_admin!";
    public static final String WRONG_PASSWORD="pass.1234";

    // Register and user

    public static final String VALID_NAME="Ivan";
    public static final String VALID_USERNAME="ivanivic";
    public static final String VALID_SURNAME="Ivic";
    public static final String VALID_PASSWORD="password.123";
    public static final String INVALID_NAME="Ivan!";
    public static final String INVALID_USERNAME="ivanivic?";
    public static final String INVALID_SURNAME="Ivic!";
    public static final String SHORT_PASSWORD="pas123";
    public static final String LONG_PASSWORD="p".repeat(51);
    public static final String SHORT_NAME="i";
    public static final String LONG_NAME="i".repeat(101);
    public static final String SHORT_SURNAME="i";
    public static final String LONG_SURNAME="i".repeat(101);
    public static final String SHORT_USERNAME="i";
    public static final String LONG_USERNAME="i".repeat(51);

    public static final String VALID_EMAIL="ivanivic@example.com";
    public static final String VALID_MANAGER_EMAIL="manager@example.com";
    public static final String VALID_NOTES="This is a dummy Ivan account";
    public static final String INVALID_EMAIL="ivanivic";
    public static final String INVALID_MANAGER_EMAIL="manager";
    public static final String LONG_EMAIL="i".repeat(255);
    public static final String LONG_MANAGER_EMAIL="i".repeat(255);
    public static final String LONG_NOTES="i".repeat(1001);
    public static final String VALID_ROLE="ADMIN";
    public static final String VALID_STATUS="Active";
    public static final String VALID_ID="1";
    public static final String CHANGE_ROLE="EMPLOYEE";

    // User and asset
    public static final String CHANGE_STATUS="Inactive";

    // Asset
    public static final String VALID_ASSET_NAME = "Parking Spot 16";
    public static final String VALID_LOCATION = "Floor plan 2";
    public static final String VALID_DESCRIPTION = "VIP parking";
    public static final String LONG_ASSET_NAME = "t".repeat(101);
    public static final String LONG_ASSET_LOCATION = "r".repeat(256);
    public static final String LONG_DESCRIPTION = "t".repeat(256);
    public static final String CATEGORY="Parking";

    //Asset category
    public static final String VALID_CATEGORY_NAME = "Room";
    public static final String VALID_CATEGORY_DESCRIPTION = "All company rooms ";
    public static final String VALID_BOOKING_PERIOD = "Day";
    public static final String LONG_CATEGORY_NAME = "t".repeat(101);
    public static final String LONG_CATEGORY_DESCRIPTION  = "t".repeat(256);
    public static final String CHANGE_BOOKING_PERIOD = "Hour";

    // Account info

    public static final String NEW_PASSWORD = "admin1234";


    public static final String BROWSER = "BROWSER";

    // Drivers constants
    public static final String FIREFOX = "FirefoxWebDriver";
    public static final String CHROME = "ChromeWebDriver";

}
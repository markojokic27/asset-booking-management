package api;

import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class AuthApiTest extends BaseApi{

        private static String refreshToken = null;

        @Test(priority = 1)
        void loginReturns200() {
            refreshToken = given()
                    .body("""
                {
                    "username": "user_admin",
                    "password": "admin123"
                }
                """)
                    .when()
                    .post("/v1/auth/login")
                    .then()
                    .statusCode(200)
                    .body("accessToken", notNullValue())
                    .body("refreshToken", notNullValue())
                    .extract()
                    .path("refreshToken");
        }

        @Test(priority = 2, dependsOnMethods = "loginReturns200")
        void refreshTokenReturns200() {
            given()
                    .body("""
                {
                    "refreshToken": "%s"
                }
                """.formatted(refreshToken))
                    .when()
                    .post("/v1/auth/refresh")
                    .then()
                    .statusCode(200)
                    .body("accessToken", notNullValue())
                    .body("refreshToken", notNullValue());
        }
}

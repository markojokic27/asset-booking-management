package api;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class UserApiTest extends BaseApi {

    private static final int VALID_USER_ID = 1;
    private static Integer createdUserId = null;
    private static final String USERNAME = "smoke_user_" + System.currentTimeMillis();
    private static final String EMAIL = "smoke_" + System.currentTimeMillis() + "@test.com";

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        if (createdUserId != null) {
            given()
                    .when()
                    .delete("/v1/users/" + createdUserId);
            createdUserId = null;
        }
    }

    @Test(priority = 1)
    void getUsersReturns200() {
        given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .when()
                .get("/v1/users")
                .then()
                .statusCode(200)
                .body("content", not(empty()));
    }

    @Test(priority = 2)
    void getUserByIdReturns200() {
        given()
                .when()
                .get("/v1/users/" + VALID_USER_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(VALID_USER_ID));
    }

    @Test(priority = 3)
    void createUserReturns201() {
        createdUserId = given()
                .body("""
                {
                    "username": "%s",
                    "surname": "Smoke",
                    "name": "Test",
                    "email": "%s",
                    "password": "Test123!",
                    "role": "EMPLOYEE",
                    "status": "ACTIVE",
                    "departmentId": 1,
                    "managerEmail": "manager@gmail.com",
                    "notes": "Smoke test user",
                    "benefit": "ALL"
                }
                """.formatted(USERNAME, EMAIL))
                .when()
                .post("/v1/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test(priority = 4, dependsOnMethods = "createUserReturns201")
    void updateUserReturns200() {
        given()
                .body("""
                {
                    "status": "INACTIVE",
                    "notes": "Updated by smoke test",
                    "benefit": "ALL"
                }
                """)
                .when()
                .patch("/v1/users/" + createdUserId)
                .then()
                .statusCode(200)
                .body("status", equalTo("INACTIVE"));
    }

    @Test(priority = 5, dependsOnMethods = "createUserReturns201")
    void deleteUserReturns200() {
        given()
                .when()
                .delete("/v1/users/" + createdUserId)
                .then()
                .statusCode(204);
    }

}
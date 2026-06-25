package api;

import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class DepartmentApiTest extends BaseApi {

    private static final int VALID_DEPARTMENT_ID = 1;

    @Test(priority = 1)
    void getDepartmentsReturns200() {
        given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .when()
                .get("/v1/departments")
                .then()
                .statusCode(200)
                .body("content", not(empty()));
    }

    @Test(priority = 2)
    void getDepartmentByIdReturns200() {
        given()
                .when()
                .get("/v1/departments/" + VALID_DEPARTMENT_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(VALID_DEPARTMENT_ID));
    }

    @Test(priority = 3)
    void updateDepartmentReturns200() {
        given()
                .body("""
                {
                  "managerId": 1
                }
                """)
                .when()
                .patch("/v1/departments/" + VALID_DEPARTMENT_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(VALID_DEPARTMENT_ID));
    }

}
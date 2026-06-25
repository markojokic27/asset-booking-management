package api;

import org.testng.annotations.Test;


public class ReportApiTest extends BaseApi {

    @Test
    void getReportsReturns200() {
        given()
                .when()
                .get("/v1/reports")
                .then()
                .statusCode(200);
    }
}
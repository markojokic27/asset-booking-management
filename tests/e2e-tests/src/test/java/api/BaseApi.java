package api;

import constants.CommonConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseApi {

    protected static final String BASE_URL = "http://localhost:8080";
    private static String token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        token = getToken();
    }

    private String getToken() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(CommonConstants.ADMIN_USERNAME, CommonConstants.ADMIN_PASS))
                .when()
                .post("/v1/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    protected RequestSpecification given() {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }
}

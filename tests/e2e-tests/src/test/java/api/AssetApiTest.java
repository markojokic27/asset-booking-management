package api;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AssetApiTest extends BaseApi {

    private static final int VALID_ASSET_ID = 1;
    private static Integer createdAssetId = null;
    private static final String ASSET_NAME = "Mac_" + System.currentTimeMillis();

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        if (createdAssetId != null) {
            given()
                    .when()
                    .delete("/v1/assets/" + createdAssetId)
                    .then()
                    .statusCode(anyOf(equalTo(204), equalTo(404)));
            createdAssetId = null;
        }
    }

    @Test(priority = 1)
    void getAssetsReturns200() {
        given()
                .queryParam("page", 1)
                .queryParam("size", 50)
                .when()
                .get("/v1/assets")
                .then()
                .statusCode(200)
                .body("content", not(empty()));
    }

    @Test(priority = 2)
    void getAssetsByIdReturns200() {
        given()
                .when()
                .get("/v1/assets/" + VALID_ASSET_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(VALID_ASSET_ID));
    }

    @Test(priority = 3)
    void createAssetReturns201() {
        createdAssetId = given()
                .body("""
                {
                  "name": "%s",
                  "categoryId": 1,
                  "description": "Lightweight laptop",
                  "status": "ACTIVE",
                  "location": "Room 2"
                }
                """.formatted(ASSET_NAME))
                .when()
                .post("/v1/assets")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test(priority = 4, dependsOnMethods = "createAssetReturns201")
    void updateAssetReturns200() {
        given()
                .body("""
                {
                  "name": "%s",
                  "categoryId": 1,
                  "description": "Lightweight laptop",
                  "status": "ACTIVE",
                  "location": "Room 3"
                }
                """.formatted(ASSET_NAME))
                .when()
                .patch("/v1/assets/" + createdAssetId)
                .then()
                .statusCode(200)
                .body("location", equalTo("Room 3"));
    }

    @Test(priority = 5, dependsOnMethods = "createAssetReturns201")
    void deleteAssetReturns200() {
        given()
                .when()
                .delete("/v1/assets/" + createdAssetId)
                .then()
                .statusCode(204);
    }

    @Test(priority = 6)
    void getAssetQrCodeReturns200() {
        given()
                .when()
                .get("/v1/assets/" + VALID_ASSET_ID + "/qr-code")
                .then()
                .statusCode(200)
                .contentType("image/png");
    }

}
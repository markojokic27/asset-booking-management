package api;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AssetCategoryApiTest extends BaseApi {

    private static final int VALID_ASSET_CATEGORY_ID = 1;
    private static Integer createdAssetCategoryId = null;
    private static final String CATEGORY_NAME = "Room_" + System.currentTimeMillis();

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        if (createdAssetCategoryId != null) {
            given()
                    .when()
                    .delete("/v1/asset-categories/" + createdAssetCategoryId)
                    .then()
                    .statusCode(anyOf(equalTo(204), equalTo(404)));
            createdAssetCategoryId = null;
        }
    }

    @Test(priority = 1)
    void getAssetCategoriesReturns200() {
        given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .when()
                .get("/v1/asset-categories")
                .then()
                .statusCode(200)
                .body("content", not(empty()));
    }

    @Test(priority = 2)
    void getAssetCategoriesByIdReturns200() {
        given()
                .when()
                .get("/v1/asset-categories/" + VALID_ASSET_CATEGORY_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(VALID_ASSET_CATEGORY_ID));
    }

    @Test(priority = 3)
    void createAssetCategoryReturns201() {
        createdAssetCategoryId = given()
                .body("""
                {
                  "name": "%s",
                  "description": "All rooms in company",
                  "bookingPeriod": "HOUR",
                  "approval": false
                }
                """.formatted(CATEGORY_NAME))
                .when()
                .post("/v1/asset-categories")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test(priority = 4, dependsOnMethods = "createAssetCategoryReturns201")
    void updateAssetCategoryReturns200() {
        given()
                .body("""
                {
                  "name": "%s",
                  "description": "All rooms in company",
                  "bookingPeriod": "HOUR",
                  "approval": true
                }
                """.formatted(CATEGORY_NAME))
                .when()
                .patch("/v1/asset-categories/" + createdAssetCategoryId)
                .then()
                .statusCode(200)
                .body("approval", equalTo(true));
    }

    @Test(priority = 5, dependsOnMethods = "createAssetCategoryReturns201")
    void deleteAssetCategoryReturns200() {
        given()
                .when()
                .delete("/v1/asset-categories/" + createdAssetCategoryId)
                .then()
                .statusCode(204);
    }

}
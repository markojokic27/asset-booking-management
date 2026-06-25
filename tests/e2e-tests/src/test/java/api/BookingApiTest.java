package api;

import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;

public class BookingApiTest extends BaseApi {

    private static final int VALID_BOOKING_ID = 1;
    private static Integer createdBookingId = null;
    private static final String BOOKING_START = LocalDateTime.now().plusYears(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    private static final String BOOKING_END = LocalDateTime.now().plusYears(2).plusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

    @Test(priority = 1)
    void getBookingsReturns200() {
        given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .when()
                .get("/v1/bookings")
                .then()
                .statusCode(200)
                .body("content", not(empty()));
    }

    @Test(priority = 2)
    void getBookingByIdReturns200() {
        given()
                .when()
                .get("/v1/bookings/" + VALID_BOOKING_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(VALID_BOOKING_ID));
    }

    @Test(priority = 3)
    void createBookingReturns201() {
        createdBookingId = given()
                .body("""
                {
                  "userId": 1,
                  "assetId": 1,
                  "bookingStart": "%s",
                  "bookingEnd": "%s",
                  "notes": "Smoke test booking"
                }
                """.formatted(BOOKING_START, BOOKING_END))
                .when()
                .post("/v1/bookings")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test(priority = 4, dependsOnMethods = "createBookingReturns201")
    void updateBookingReturns200() {
        given()
                .body("""
                {
                  "userId": 1,
                  "assetId": 1,
                  "bookingStart": "%s",
                  "bookingEnd": "%s",
                  "notes": "Smoke test booking updated"
                }
                """.formatted(BOOKING_START, BOOKING_END))
                .when()
                .patch("/v1/bookings/" + createdBookingId)
                .then()
                .statusCode(200)
                .body("notes", equalTo("Smoke test booking updated"));
    }

}
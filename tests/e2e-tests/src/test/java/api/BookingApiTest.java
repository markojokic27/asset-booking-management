package api;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.*;

public class BookingApiTest extends BaseApi {

    private static final int VALID_BOOKING_ID = 26;
    private static Integer createdBookingId = null;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final LocalDateTime BOOKING_START_DT = LocalDateTime.now()
            .plusYears(2)
            .plusMinutes(ThreadLocalRandom.current().nextInt(0, 100_000));
    private static final LocalDateTime BOOKING_END_DT = BOOKING_START_DT.plusDays(5);

    private static final String BOOKING_START = BOOKING_START_DT.format(FORMATTER);
    private static final String BOOKING_END = BOOKING_END_DT.format(FORMATTER);

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

    @AfterClass
    void cleanup() {
        if (createdBookingId != null) {
            try {
                given().when().delete("/v1/bookings/" + createdBookingId);
            } catch (Exception ignored) {
            }
        }
    }
}
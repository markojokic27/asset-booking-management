package simulations;

import constants.CommonConstants;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BookingWriteSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");

    ChainBuilder login = exec(
            http("Login")
                    .post("/v1/auth/login")
                    .body(StringBody("""
                    {"username": "%s", "password": "%s"}
                    """.formatted(CommonConstants.ADMIN_USERNAME, CommonConstants.ADMIN_PASS)))
                    .check(status().is(200))
                    .check(jsonPath("$.accessToken").saveAs("accessToken"))
    );

    ScenarioBuilder createAndCleanupBooking = scenario("Create and cleanup booking")
            .exec(login)
            .exec(session -> {
                int month = 1 + (int) (session.userId() % 12);
                String monthStr = String.format("%02d", month);
                session = session.set("bookingStart", "2027-" + monthStr + "-01T10:00:00Z");
                session = session.set("bookingEnd", "2027-" + monthStr + "-01T11:00:00Z");
                return session;
            })
            .exec(
                    http("Create booking")
                            .post("/v1/bookings")
                            .header("Authorization", "Bearer #{accessToken}")
                            .body(StringBody("""
                                {
                                  "userId": 1,
                                  "assetId": 1,
                                  "bookingStart": "#{bookingStart}",
                                  "bookingEnd": "#{bookingEnd}",
                                  "notes": "gatling load test booking"
                                }
                                """))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("bookingId"))
            )
            .pause(1)
            .doIf(session -> session.contains("bookingId")).then(
                    exec(
                            http("Cancel booking (cleanup)")
                                    .patch("/v1/bookings/#{bookingId}")
                                    .header("Authorization", "Bearer #{accessToken}")
                                    .body(StringBody("""
                                        {
                                          "userId": 1,
                                          "assetId": 1,
                                          "bookingStart": "#{bookingStart}",
                                          "bookingEnd": "#{bookingEnd}",
                                          "notes": "gatling load test booking - cancelled",
                                          "status": "CANCELLED"
                                        }
                                        """))
                                    .check(status().is(200))
                    )
            );

    {
        setUp(
                createAndCleanupBooking.injectOpen(rampUsers(10).during(20))
        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().percentile(95).lt(1000),
                        global().failedRequests().percent().lt(1.0)
                );
    }
}
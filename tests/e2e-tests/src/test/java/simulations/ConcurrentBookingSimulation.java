package simulations;

import constants.CommonConstants;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ConcurrentBookingSimulation extends Simulation {

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
    ScenarioBuilder concurrentBookingScenario = scenario("Concurrent same asset booking")
            .exec(login)
            .exec(
                    http("Book same slot simultaneously")
                            .post("/v1/bookings")
                            .header("Authorization", "Bearer #{accessToken}")
                            .body(StringBody("""
                                {
                                  "userId": 1,
                                  "assetId": 1,
                                  "bookingStart": "2031-06-15T10:00:00Z",
                                  "bookingEnd": "2031-06-15T11:00:00Z",
                                  "notes": "concurrent test booking"
                                }
                                """))
                            .check(status().in(201, 409))
            );

    {
        setUp(
                concurrentBookingScenario.injectOpen(atOnceUsers(20))
        )
                .protocols(httpProtocol)
                .assertions(
                        global().failedRequests().percent().lt(1.0)
                );
    }
}
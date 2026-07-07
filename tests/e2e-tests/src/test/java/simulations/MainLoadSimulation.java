package simulations;

import constants.CommonConstants;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class MainLoadSimulation extends Simulation {

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

    ScenarioBuilder usersScenario = scenario("Users - read flow")
            .exec(login)
            .exec(
                    http("Get users list")
                            .get("/v1/users?page=1&size=14")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            )
            .pause(1)
            .exec(
                    http("Get user by id")
                            .get("/v1/users/1")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            );

    ScenarioBuilder assetsScenario = scenario("Assets - read flow")
            .exec(login)
            .exec(
                    http("Get assets list")
                            .get("/v1/assets?page=1&size=59")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            )
            .pause(1)
            .exec(
                    http("Get asset by id")
                            .get("/v1/assets/1")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            )
            .pause(1)
            .exec(
                    http("Get asset QR code")
                            .get("/v1/assets/1/qr-code")
                            .header("Authorization", "Bearer #{accessToken}")
                            .header("Accept", "image/png")
                            .check(status().is(200))
            );

    ScenarioBuilder assetCategoriesScenario = scenario("Asset categories - read flow")
            .exec(login)
            .exec(
                    http("Get asset categories")
                            .get("/v1/asset-categories?page=1&size=10")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            );

    ScenarioBuilder bookingsScenario = scenario("Bookings - read flow")
            .exec(login)
            .exec(
                    http("Get bookings list")
                            .get("/v1/bookings?page=1&size=21")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            )
            .pause(1)
            .exec(
                    http("Get booking by id")
                            .get("/v1/bookings/26")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            );

    ScenarioBuilder reportsScenario = scenario("Reports - read flow")
            .exec(login)
            .exec(
                    http("Get reports")
                            .get("/v1/reports")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            );

    {
        setUp(
                usersScenario.injectOpen(rampUsers(20).during(30)),
                assetsScenario.injectOpen(rampUsers(30).during(30)),
                assetCategoriesScenario.injectOpen(rampUsers(10).during(30)),
                bookingsScenario.injectOpen(rampUsers(20).during(30)),
                reportsScenario.injectOpen(rampUsers(5).during(30))
        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().percentile(95).lt(700),
                        global().failedRequests().percent().lt(1.0)
                );
    }
}
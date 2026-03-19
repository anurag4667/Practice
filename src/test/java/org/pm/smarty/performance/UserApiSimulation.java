package org.pm.smarty.performance;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class UserApiSimulation extends Simulation {

    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:4000");
    private static final int CREATE_USERS = Integer.getInteger("users", 100);
    private static final int RAMP_SECONDS = Integer.getInteger("rampSeconds", 30);
    private static final int READ_RPS = Integer.getInteger("readRps", 20);
    private static final int READ_SECONDS = Integer.getInteger("readSeconds", 60);
    private static final int P95_THRESHOLD_MS = Integer.getInteger("p95Ms", 1000);
    private static final double SUCCESS_RATE_THRESHOLD = Double.parseDouble(
            System.getProperty("successRate", "99.0")
    );

    private static Iterator<Map<String, Object>> userFeeder() {
        return Stream.generate(() -> {
            String nonce = UUID.randomUUID().toString().substring(0, 8);
            return Map.<String, Object>of(
                    "name", "Perf User " + nonce,
                    "email", "perf." + nonce + "@example.com",
                    "password", "P@ssword123"
            );
        }).iterator();
    }

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private final ScenarioBuilder createAndFetchUser = scenario("Create and fetch user")
            .feed(userFeeder())
            .exec(
                    http("POST /user")
                            .post("/user")
                            .body(io.gatling.javaapi.core.CoreDsl.StringBody(session ->
                                    """
                                    {
                                      "name": "%s",
                                      "email": "%s",
                                      "password": "%s"
                                    }
                                    """.formatted(
                                            session.getString("name"),
                                            session.getString("email"),
                                            session.getString("password")
                                    )
                            ))
                            .check(status().is(200))
                            .check(jsonPath("$.userid").saveAs("userId"))
            )
            .exec(
                    http("GET /user/{id}")
                            .get("/user/#{userId}")
                            .check(status().is(200))
            );

    private final ScenarioBuilder listUsers = scenario("List users")
            .exec(
                    http("GET /user")
                            .get("/user")
                            .check(status().is(200))
            );

    {
        setUp(
                createAndFetchUser.injectOpen(
                        rampUsers(CREATE_USERS).during(Duration.ofSeconds(RAMP_SECONDS))
                ),
                listUsers.injectOpen(
                        constantUsersPerSec(READ_RPS).during(Duration.ofSeconds(READ_SECONDS))
                )
        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().percentile4().lt(P95_THRESHOLD_MS),
                        global().successfulRequests().percent().gt(SUCCESS_RATE_THRESHOLD)
                );
    }
}

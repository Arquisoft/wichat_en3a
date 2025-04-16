package com.uniovi.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class WichatSimulation extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8000")
            .inferHtmlResources()
            .acceptHeader("*/*")
            .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:135.0) Gecko/20100101 Firefox/135.0");

    // Common headers
    private Map<CharSequence, String> htmlHeaders = Map.of(
            "Accept", "text/html,application/xhtml+xml",
            "Upgrade-Insecure-Requests", "1"
    );

    private Map<CharSequence, String> resourceHeaders = Map.of(
            "Accept", "image/webp,image/*,*/*;q=0.8"
    );

    private ScenarioBuilder scn = scenario("WichatSimulation")
            .exec(
                    http("Login Page")
                            .get("/login")
                            .headers(htmlHeaders)
                            .resources(
                                    // Main page resources
                                    http("Login CSS").get("/css/main.css"),
                                    http("Login Form CSS").get("/css/forms.css"),
                                    http("Logo").get("/images/qwizz.png")
                            )
            )
            .pause(3)
            .exec(
                    http("Submit Login")
                            .post("/login")
                            .headers(htmlHeaders)
                            .formParam("username", "a@a.es")
                            .formParam("password", "a")
                            .formParam("_csrf", "CSRF_TOKEN_PLACEHOLDER")
                            .resources(
                                    http("Home CSS").get("/css/home.css"),
                                    http("Nav CSS").get("/css/nav.css")
                            )
            )
            .pause(4)
            .exec(
                    http("Scores Page")
                            .get("/user/scores")
                            .headers(htmlHeaders)
                            .resources(
                                    http("Scores CSS").get("/css/scores.css")
                            )
            )
            .pause(2)
            .exec(
                    http("Home Page")
                            .get("/home")
                            .headers(htmlHeaders)
            )
            .pause(1)
            .exec(
                    http("Logout")
                            .get("/logout")
                            .headers(htmlHeaders)
            );

    {
        setUp(
                scn.injectOpen(
                        atOnceUsers(1),                          // Initial verification user (5 sec)
                        rampUsers(5).during(10),                 // Gentle warm-up (10 sec)
                        rampUsers(15).during(30),                // Gradual increase (30 sec)
                        rampUsers(50).during(60),                // Main load phase (1 min)
                        rampUsers(100).during(120),              // Stress phase (2 min)
                        constantUsersPerSec(20).during(300)      // Sustained load (5 min)
                ))
                .protocols(httpProtocol);
    }
}

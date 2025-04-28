package com.uniovi.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

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

    private Map<CharSequence, String> ajaxHeaders = Map.of(
            "Accept", "*/*",
            "X-Requested-With", "XMLHttpRequest"
    );

    private HttpRequestActionBuilder getLogin = http("Login Page")
                            .get("/login")
                            .headers(htmlHeaders)
                            .resources(
                                    http("Login CSS").get("/css/main.css"),
                                    http("Login Form CSS").get("/css/forms.css"),
                                    http("Logo").get("/images/qwizz.png")
                            );
    private HttpRequestActionBuilder login = http("Submit Login")
                            .post("/login")
                            .headers(htmlHeaders)
                            .formParam("username", "a@a.es")
                            .formParam("password", "a")
                            .formParam("_csrf", "CSRF_TOKEN_PLACEHOLDER")
                            .resources(
                                    http("Main CSS").get("/css/main.css"),
                                    http("Forms CSS").get("/css/forms.css"),
                                    http("Nav CSS").get("/css/nav.css"),
                                    http("Home CSS").get("/css/home.css")
                            );
    private HttpRequestActionBuilder scores = http("Scores Page")
                            .get("/user/scores")
                            .headers(htmlHeaders)
                            .resources(
                                    http("Scores CSS").get("/css/scores.css")
                            );

    private HttpRequestActionBuilder home = http("Home Page")
                            .get("/home")
                            .headers(htmlHeaders);

    private HttpRequestActionBuilder logout = http("Logout")
                            .get("/logout")
                            .headers(htmlHeaders);

    private HttpRequestActionBuilder startBiologyGame = http("Start Biology Game")
                            .get("/game/start/BIOLOGY")
                            .headers(htmlHeaders)
                            .resources(
                                http("Questions CSS").get("/css/questions.css"),
                                http("Chat CSS").get("/css/chat.css"),
                                http("Question JS").get("/script/question.js")
                            );
    private HttpRequestActionBuilder resultsGame = http("View Results")
                            .get("/game/next")
                            .headers(htmlHeaders)
                            .resources(
                                http("Results CSS").get("/css/results.css")
                            );

    private ScenarioBuilder scn = scenario("Login and see scores")
            .exec(getLogin).pause(3)
            .exec(login).pause(4)
            .exec(scores).pause(2)
            .exec(home).pause(1)
            .exec(logout);

    private ScenarioBuilder scn2 = scenario("Play game")
            .exec(login).pause(5)
            .exec(startBiologyGame).pause(1)
            .repeat(10, "Pass question").on(exec().pause(15))
            .exec(resultsGame).pause(5)
            .exec(scores).pause(3)
            .exec(home);

    {
        setUp(scn.injectOpen(
                        atOnceUsers(1),                          // Initial verification user (5 sec)
                        rampUsers(5).during(10),          // Gentle warm-up (10 sec)
                        rampUsers(15).during(30),                // Gradual increase (30 sec)
                        rampUsers(50).during(60),                // Main load phase (1 min)
                        rampUsers(100).during(120),              // Stress phase (2 min)
                        constantUsersPerSec(20).during(300)      // Sustained load (5 min)
                )).protocols(httpProtocol);

        setUp(scn2.injectOpen(
                        atOnceUsers(1),                          // Initial verification user (5 sec)
                        rampUsers(5).during(10),          // Gentle warm-up (10 sec)
                        rampUsers(15).during(30),                // Gradual increase (30 sec)
                        rampUsers(50).during(60),                // Main load phase (1 min)
                        rampUsers(100).during(120),              // Stress phase (2 min)
                        constantUsersPerSec(20).during(300)      // Sustained load (5 min)
                )).protocols(httpProtocol);
    }
}

package com.uniovi.wichatwebapp.acceptance.cucumber.game;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/game.feature",
        glue= {"com.uniovi.wichatwebapp.acceptance.cucumber.game",
        "com.uniovi.wichatwebapp.acceptance.cucumber.config"}
)
public class GameTest {

}

package com.uniovi.wichatwebapp.acceptance.cucumber.scores;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/scores.feature",
        glue= {"com.uniovi.wichatwebapp.acceptance.cucumber.scores",
        "com.uniovi.wichatwebapp.acceptance.cucumber.config"}
)
public class ScoreTest {

}

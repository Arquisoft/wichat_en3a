package com.uniovi.wichatwebapp.acceptance;


import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/",
        glue = {"com.uniovi.wichatwebapp.acceptance.steps",
                "com.uniovi.wichatwebapp.acceptance"}
)
public class SignUpTest {

}

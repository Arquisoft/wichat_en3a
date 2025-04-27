package com.uniovi.wichatwebapp.acceptance.cucumber.signup;


import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/signup.feature",
        glue= {"com.uniovi.wichatwebapp.acceptance.cucumber.signup",
        "com.uniovi.wichatwebapp.acceptance.cucumber.config"}
)
public class SignUpTest {

}

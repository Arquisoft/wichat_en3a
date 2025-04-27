package com.uniovi.wichatwebapp.acceptance.cucumber.login;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/login.feature",
        glue= {"com.uniovi.wichatwebapp.acceptance.cucumber.login",
        "com.uniovi.wichatwebapp.acceptance.cucumber.config"}
)
public class LoginTest {

}

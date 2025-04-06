package com.uniovi.wichatwebapp.acceptance.steps;

import com.uniovi.wichatwebapp.acceptance.BaseStep;
import com.uniovi.wichatwebapp.acceptance.PO.PO_LoginView;
import com.uniovi.wichatwebapp.acceptance.PO.PO_SignUpView;
import com.uniovi.wichatwebapp.acceptance.util.SeleniumUtils;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpSteps extends BaseStep {

    private String email;
    private String username;
    private String password;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup(); // Automatically manage ChromeDriver
        options = new ChromeOptions();
        //options.addArguments("--start-maximized", "--disable-infobars", "--remote-allow-origins=*", "--headless"); // Open browser in maximized mode, disable Chrome's info bars and allow cross-origin requests
        //options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Disable automation banner
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        options.setExperimentalOption("detach", true); // Keep the browser open after the test
        //System.out.println("Running AcceptanceTests...");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void go(){
        driver.navigate().to(BASE_URL);
    }

    @Given("^A user with email \"(.+)\", name \"(.+)\" and password \"(.+)\"$")
    public void an_incorrect_email(String email, String name, String password) {
        this.email = email;
        this.username = name;
        this.password = password;
    }

    @When("^I fill the data in the sign-up form and click Sing up$")
    public void signing_up(){
        PO_LoginView.goToSignUpPage(driver);
        PO_SignUpView.fillSignUpForm(driver, email, username, password);
    }

    @Then("^The message \"(.+)\" is shown$")
    public void the_message_is_shown(String message){
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + message + "')]")));
        Assertions.assertNotNull(success);
    }

}

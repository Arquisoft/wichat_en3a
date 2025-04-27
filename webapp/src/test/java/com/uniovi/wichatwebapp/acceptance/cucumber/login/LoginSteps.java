package com.uniovi.wichatwebapp.acceptance.cucumber.login;

import com.uniovi.wichatwebapp.acceptance.cucumber.BaseStep;
import com.uniovi.wichatwebapp.acceptance.PO.PO_LoginView;
import com.uniovi.wichatwebapp.acceptance.PO.PO_SignUpView;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginSteps extends BaseStep {

    @BeforeAll
    public static void setUp() {
        setUpDriver();
    }


    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void goToStart() {
        driver.navigate().to(BASE_URL);
    }

    @Given("A user with email {string} and password {string}")
    public void user_credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Given("A user with email {string}, name {string} and password {string}")
    public void user_credentials_register(String email, String name, String password) {
        this.email = email;
        this.username = name;
        this.password = password;
    }

    @When("They register and fill the data in the login form")
    public void register_and_login(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Login']")));
        PO_LoginView.goToSignUpPage(driver, wait);

        PO_SignUpView.fillSignUpForm(driver, email, username, password);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("name")));
        PO_LoginView.fillLoginForm(driver, email, password);
    }

    @When("They fill the data in the login form")
    public void fill_login_form() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Login']")));
        PO_LoginView.fillLoginForm(driver, email, password);

    }

    @Then("The message {string} is shown")
    public void the_message_is_shown(String message) {
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + message + "')]")));
        assertNotNull(success);

        driver.navigate().to(BASE_URL);
    }

    @Then("They are redirected to the home page")
    public void redirected_to_home() {
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + "Sport Game" + "')]")));
        assertNotNull(success);

        driver.navigate().to(BASE_URL);
    }


}

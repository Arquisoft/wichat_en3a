package com.uniovi.wichatwebapp.acceptance.cucumber.scores;

import com.uniovi.wichatwebapp.acceptance.cucumber.BaseStep;
import com.uniovi.wichatwebapp.acceptance.PO.PO_HomeView;
import com.uniovi.wichatwebapp.acceptance.PO.PO_LoginView;
import com.uniovi.wichatwebapp.acceptance.PO.PO_SignUpView;
import com.uniovi.wichatwebapp.acceptance.util.SeleniumUtils;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ScoreSteps extends BaseStep {


    @BeforeAll
    public static void setUp() {
        setUpDriver();


        driver.navigate().to(BASE_URL);

        PO_LoginView.registerAndLogin(driver, wait, "scoresTest@correcto.com", "Test", "passwd");

    }


    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void goToStart() {

        this.email = "scoresTest@correcto.com";
        this.username = "Test";
        this.password = "passwd";

        driver.navigate().to(BASE_URL);
    }

    @Given("A registered user")
    public void user_credentials() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Login']")));
        PO_LoginView.fillLoginForm(driver, email, password);
    }

    @When("They check their scores")
    public void they_check_their_scores() {

        PO_HomeView.goToScoresPage(driver, wait);
    }

    @Then("An empty list is shown along with a message")
    public void an_empty_list_is_shown_and_the_message_appears() {
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'no-scores')]")));
        Assertions.assertNotNull(result);
    }

}

package com.uniovi.wichatwebapp.acceptance.cucumber.game;

import com.uniovi.wichatwebapp.acceptance.PO.PO_GameView;
import com.uniovi.wichatwebapp.acceptance.PO.PO_HomeView;
import com.uniovi.wichatwebapp.acceptance.PO.PO_LoginView;
import com.uniovi.wichatwebapp.acceptance.cucumber.BaseStep;
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

public class GameSteps extends BaseStep {

    @BeforeAll
    public static void setUp() {
        setUpDriver();

        driver.navigate().to(BASE_URL);

        PO_LoginView.registerAndLogin(driver, wait, "gameTest@correcto.com", "Test", "passwd");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void goToStart() {
        this.email = "gameTest@correcto.com";
        this.username = "Test";
        this.password = "passwd";

        driver.navigate().to(BASE_URL);
    }


    @Given("A registered user")
    public void a_registered_user(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Login']")));
        PO_LoginView.fillLoginForm(driver, email, password);
    }

    @When("They start a custom game with 1 question and the first category")
    public void user_start_game(){
        PO_HomeView.selectCustomGame(driver, wait);
        PO_GameView.fillCustomGameForm(driver, 1, 30, 1);
    }

    @When("They start a custom game with 1 question and the first category, and they select an answer")
    public void user_plays_game(){
        PO_HomeView.selectCustomGame(driver, wait);
        PO_GameView.fillCustomGameForm(driver, 1, 30, 1);
        PO_GameView.selectAnswer(driver, wait,1);
    }

    @When("They finish a game and click on Play Again")
    public void user_replays_game(){
        PO_HomeView.selectCustomGame(driver, wait);
        PO_GameView.fillCustomGameForm(driver, 1, 30, 1);
        PO_GameView.selectAnswer(driver, wait,1);
        PO_GameView.selectPlayAgainGame(driver, wait);
    }

    @When("They finish a game and click on Check your scores")
    public void user_checkResults_game(){
        PO_HomeView.selectCustomGame(driver, wait);
        PO_GameView.fillCustomGameForm(driver, 1, 30, 1);
        PO_GameView.selectAnswer(driver, wait,1);
        PO_GameView.selectCheckResultsGame(driver, wait);
    }

    @Then("The game starts")
    public void game_starts(){
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'game-container')]")));
        Assertions.assertNotNull(result);
    }

    @Then("The game results page appears")
    public void game_results_appear(){
        SeleniumUtils.textIsPresentOnPage(driver, "Results");
    }

    @Then("The score list is shown")
    public void score_list_is_shown(){
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[contains(@class,'pure-bg-table')]")));
        Assertions.assertNotNull(result);
    }


}

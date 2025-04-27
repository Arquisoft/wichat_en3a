package com.uniovi.wichatwebapp.acceptance.PO;

import com.uniovi.wichatwebapp.acceptance.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PO_GameView extends PO_View{



    public static void fillCustomGameForm(WebDriver driver, int categoryIndex, int duration, int numberQuestions){
        new Select(driver.findElement(By.id("category"))).selectByIndex(categoryIndex);

        WebElement timer = driver.findElement(By.name("timerSeconds"));
        timer.click();
        timer.clear();
        timer.sendKeys(String.valueOf(duration));

        WebElement questionCount = driver.findElement(By.name("questionCount"));
        questionCount.click();
        questionCount.clear();
        questionCount.sendKeys(String.valueOf(numberQuestions));

        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    public static void selectAnswer(WebDriver driver, WebDriverWait wait, int index){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'game-container')]")));

        List<WebElement> answers = driver.findElements(By.xpath("//button[contains(@class, 'answer-button')]"));
        answers.get(index).click();

        SeleniumUtils.waitSeconds(driver, 3);

    }

    public static void selectPlayAgainGame(WebDriver driver, WebDriverWait wait){
        SeleniumUtils.textIsPresentOnPage(driver, "Results");
        WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/game/start/SPORT/30/1')]"));
        element.click();
    }

    public static void selectCheckResultsGame(WebDriver driver, WebDriverWait wait){
        SeleniumUtils.textIsPresentOnPage(driver, "Results");
        WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/user/scores')]"));
        element.click();
    }
}

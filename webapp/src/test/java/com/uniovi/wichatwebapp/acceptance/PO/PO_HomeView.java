package com.uniovi.wichatwebapp.acceptance.PO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PO_HomeView extends PO_View{

    public static void goToScoresPage(WebDriver driver, WebDriverWait wait) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, '/user/scores')]")));

        WebElement element = driver.findElement(By.xpath("//a[contains(@href, '/user/scores')]"));

        element.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[contains(@class, 'pure-bg-table')]")));
    }

    public static void selectCustomGame(WebDriver driver, WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, '/game/personalized')]")));

        WebElement element = driver.findElement(By.xpath("//a[contains(@href, '/game/personalized')]"));

        element.click();

    }

    public static void selectGameCategory(WebDriver driver, WebDriverWait wait, String category){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[normalize-space(text())='" + category + "']")));

        WebElement element = driver.findElement(By.xpath("//a[text()='" + category + "']"));
        element.click();
    }

}

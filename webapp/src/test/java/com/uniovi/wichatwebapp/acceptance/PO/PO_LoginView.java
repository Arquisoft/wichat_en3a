package com.uniovi.wichatwebapp.acceptance.PO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PO_LoginView extends PO_View{


    public static void goToSignUpPage(WebDriver driver, WebDriverWait wait) {
        List<WebElement> elements = checkElementBy(driver, "text","Sign Up");
        elements.get(0).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
    }

    public static void fillLoginForm(WebDriver driver, String emailp, String passwordp){
        WebElement email = driver.findElement(By.name("username"));
        email.click();
        email.clear();
        email.sendKeys(emailp);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);

        driver.findElement(By.className("big-button")).click();
    }

    public static void registerAndLogin(WebDriver driver, WebDriverWait wait, String email, String username, String password) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Login']")));
        PO_LoginView.goToSignUpPage(driver, wait);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        PO_SignUpView.fillSignUpForm(driver, email, username, password);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("name")));
        PO_LoginView.fillLoginForm(driver, email, password);

    }
}

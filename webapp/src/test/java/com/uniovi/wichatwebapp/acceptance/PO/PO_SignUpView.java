package com.uniovi.wichatwebapp.acceptance.PO;

import com.uniovi.wichatwebapp.acceptance.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_SignUpView extends PO_View{


    public static void fillSignUpForm(WebDriver driver, String emailp, String namep, String passwordp) {
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.clear();
        email.sendKeys(emailp);
        WebElement name = driver.findElement(By.name("name"));
        name.click();
        name.clear();
        name.sendKeys(namep);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);

        driver.findElement(By.className("big-button")).click();
    }

}

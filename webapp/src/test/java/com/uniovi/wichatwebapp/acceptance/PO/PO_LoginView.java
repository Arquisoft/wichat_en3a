package com.uniovi.wichatwebapp.acceptance.PO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_LoginView extends PO_View{


    public static void goToSignUpPage(WebDriver driver){
        List<WebElement> elements = checkElementBy(driver, "text","Sign Up");
        elements.get(0).click();
    }

}

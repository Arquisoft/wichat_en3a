package com.uniovi.wichatwebapp.acceptance.cucumber;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public abstract class BaseStep {

    protected static final String BASE_URL = "http://localhost:8000/login";

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static ChromeOptions options;

    protected String email;
    protected String username;
    protected String password;


    protected static void setUpDriver(){
        WebDriverManager.chromedriver().setup(); // Automatically manage ChromeDriver
        options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-infobars", "--remote-allow-origins=*", "--headless"); // Open browser in maximized mode, disable Chrome's info bars and allow cross-origin requests
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Disable automation banner
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        options.setExperimentalOption("detach", true); // Keep the browser open after the test
        //System.out.println("Running AcceptanceTests...");

    }


}

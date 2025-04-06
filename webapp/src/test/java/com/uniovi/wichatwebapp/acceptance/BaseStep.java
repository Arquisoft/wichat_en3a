package com.uniovi.wichatwebapp.acceptance;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

@SpringBootTest
public abstract class BaseStep {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static ChromeOptions options;
    protected static final String BASE_URL = "http://localhost:8000";


}

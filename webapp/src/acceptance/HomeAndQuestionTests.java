package com.uniovi.wichatwebapp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HomeAndQuestionTests {

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup(); // Automatically manage ChromeDriver
        System.out.println("Running HomeAndQuestionTests...");
    }

    @Test
    public void testLoginAndNavigateToQuestion() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Open browser in maximized mode
        options.addArguments("--disable-infobars"); // Disable Chrome's info bars
        options.addArguments("--remote-allow-origins=*"); // Allow cross-origin requests
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Disable automation banner
        options.setExperimentalOption("detach", true); // Keep the browser open after the test

        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the login page
            driver.get("http://localhost:8000/login");
            System.out.println("Navigated to Login Page");
            Thread.sleep(1000); // Wait 1 second

            // Log in with newuser credentials
            WebElement emailField = driver.findElement(By.name("username")); // Match 'name' attribute
            WebElement passwordField = driver.findElement(By.name("password")); // Match 'name' attribute
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']")); // Match the submit button

            emailField.sendKeys("newuser@example.com");
            System.out.println("Entered email");
            Thread.sleep(1000); // Wait 1 second
            passwordField.sendKeys("password123");
            System.out.println("Entered password");
            Thread.sleep(1000); // Wait 1 second
            loginButton.click();
            System.out.println("Clicked login button");
            Thread.sleep(2000); // Wait 2 seconds for redirection

            // Verify redirection to /home
            if (driver.getCurrentUrl().contains("/home")) {
                System.out.println("Redirected to /home");
            } else {
                System.out.println("Failed to redirect to /home");
                return;
            }

            // Click one of the buttons on home.html (e.g., Sport Game)
            WebElement sportGameButton = driver.findElement(By.linkText("Sport Game"));
            sportGameButton.click();
            System.out.println("Clicked Sport Game button");
            Thread.sleep(2000); // Wait 2 seconds for redirection

            // Verify redirection to question.html
            if (driver.getCurrentUrl().contains("/game/start/SPORT")) {
                System.out.println("Redirected to Sport Game questions");
            } else {
                System.out.println("Failed to redirect to Sport Game questions");
                return;
            }

            // Click one of the answers on question.html
            WebElement answerButton = driver.findElement(By.id("chooseAnswerButton")); // Match the answer button by ID
            answerButton.click();
            System.out.println("Clicked an answer button");
            Thread.sleep(2000); // Wait 2 seconds for redirection

        } finally {
            driver.quit();
        }
    }
}

package com.uniovi.wichatwebapp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup(); // Automatically manage ChromeDriver
        System.out.println("Running AcceptanceTests...");
    }

    @Test
    public void testLoginPageLoads() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testSignupAndLoginWithSelenium() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Open browser in maximized mode
        options.addArguments("--disable-infobars"); // Disable Chrome's info bars
        options.addArguments("--remote-allow-origins=*"); // Allow cross-origin requests
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Disable automation banner
        options.setExperimentalOption("detach", true); // Keep the browser open after the test

        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the signup page
            driver.get("http://localhost:8000/signup");
            System.out.println("Navigated to Signup Page");
            Thread.sleep(1000); // Wait 1 second

            // Locate signup form elements
            WebElement emailField = driver.findElement(By.name("email")); // Match 'name' attribute
            Thread.sleep(1000); // Wait 1 second
            WebElement usernameField = driver.findElement(By.name("name")); // Match 'name' attribute
            Thread.sleep(1000); // Wait 1 second
            WebElement passwordField = driver.findElement(By.name("password")); // Match 'name' attribute
            Thread.sleep(1000); // Wait 1 second
            WebElement signupButton = driver.findElement(By.cssSelector("button[type='submit']")); // Match the submit button
            Thread.sleep(1000); // Wait 1 second

            // Fill out the signup form
            emailField.sendKeys("newuser@example.com");
            System.out.println("Entered email");
            Thread.sleep(1000); // Wait 1 second
            usernameField.sendKeys("newuser");
            System.out.println("Entered username");
            Thread.sleep(1000); // Wait 1 second
            passwordField.sendKeys("password123");
            System.out.println("Entered password");
            Thread.sleep(1000); // Wait 1 second
            signupButton.click();
            System.out.println("Clicked signup button");
            Thread.sleep(1000); // Wait 1 second

            // Navigate to the login page
            driver.get("http://localhost:8000/login");
            System.out.println("Navigated to Login Page");
            Thread.sleep(1000); // Wait 1 second

            // Locate login form elements
            WebElement loginEmailField = driver.findElement(By.name("username")); // Match 'name' attribute
            Thread.sleep(1000); // Wait 1 second
            WebElement loginPasswordField = driver.findElement(By.name("password")); // Match 'name' attribute
            Thread.sleep(1000); // Wait 1 second
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']")); // Match the submit button
            Thread.sleep(1000); // Wait 1 second

            // Fill out the login form
            loginEmailField.sendKeys("newuser@example.com");
            System.out.println("Entered username");
            Thread.sleep(1000); // Wait 1 second
            loginPasswordField.sendKeys("password123");
            System.out.println("Entered password");
            Thread.sleep(1000); // Wait 1 second
            loginButton.click();
            System.out.println("Clicked login button");
            Thread.sleep(1000); // Wait 1 second

            // Verify the URL contains /home
            if (driver.getCurrentUrl().contains("/home")) {
                System.out.println("Login successful, redirected to /home");
            } else {
                System.out.println("Failed to redirect to /home");
            }
            Thread.sleep(1000); // Wait 1 second

        } finally {
            driver.quit();
        }
    }


}

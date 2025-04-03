package com.uniovi.wichatwebapp.acceptance;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTestsIT {

    @Autowired
    private MockMvc mockMvc;

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String BASE_URL = "http://localhost:8000";
    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";

    private static ChromeOptions options;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup(); // Automatically manage ChromeDriver
        options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-infobars", "--remote-allow-origins=*", "--headless"); // Open browser in maximized mode, disable Chrome's info bars and allow cross-origin requests
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Disable automation banner
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("Running AcceptanceTests...");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Order(1)
    @Test
    public void testLoginPageLoads() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    @WithMockUser(username = TEST_USERNAME, roles = {"USER"})
    public void testSignupFlow() throws InterruptedException {
        options.setExperimentalOption("detach", true); // Keep the browser open after the test


            // Navigate to the signup page
            driver.get(BASE_URL + "/signup");
            System.out.println("Navigated to Signup Page");
            Thread.sleep(1000); // Wait 1 second

            // Locate signup form elements
            // Match 'email' attribute
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
            // Match 'name' attribute
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
            // Match 'password' attribute
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
            // Match the submit button
            WebElement signupButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));


            // Fill out the signup form
            emailField.sendKeys(TEST_EMAIL);
            System.out.println("Entered email");
            Thread.sleep(1000); // Wait 1 second
            usernameField.sendKeys(TEST_USERNAME);
            System.out.println("Entered username");
            Thread.sleep(1000); // Wait 1 second
            passwordField.sendKeys(TEST_PASSWORD);
            System.out.println("Entered password");
            Thread.sleep(1000); // Wait 1 second
            signupButton.click();
            System.out.println("Clicked signup button");
            Thread.sleep(1000); // Wait 1 second

    }

    @Order(3)
    @Test
    public void testLoginWithValidCredentials() throws InterruptedException {
        driver.get(BASE_URL + "/login");
        System.out.println("Navigated to Login Page");
        Thread.sleep(1000); // Wait 1 second

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        Thread.sleep(1000); // Wait 1 second
        emailField.sendKeys(TEST_EMAIL);
        Thread.sleep(1000); // Wait 1 second
        passwordField.sendKeys(TEST_PASSWORD);
        Thread.sleep(1000); // Wait 1 second
        loginButton.click();
        System.out.println("Login form submitted");

        // Verify successful login by checking if redirected to /home (verifies if the url contains /home)
        if (driver.getCurrentUrl().contains("/home")) {
            System.out.println("Login successful, redirected to /home");
        } else {
            System.out.println("Failed to redirect to /home");
        }
        Thread.sleep(1000); // Wait 1 second
    }

    @Order(4)
    @Test
    public void testLoginWithInvalidCredentials() {
        driver.get("http://localhost:8000/login");


        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        emailField.sendKeys("wronguser@example.com");
        passwordField.sendKeys("wrongpassword");
        loginButton.click();

        // Check for error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("error")));
        assertNotNull(errorMessage, "Expected error message for invalid login");
        System.out.println("Invalid login test passed");
    }

    @Order(5)
    @Test
    public void testLogout() throws InterruptedException {
        // Log in first
        driver.get(BASE_URL + "/login");

        Thread.sleep(1000); // Wait 1 second
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        Thread.sleep(1000); // Wait 1 second
        WebElement passwordField = driver.findElement(By.name("password"));
        Thread.sleep(1000); // Wait 1 second
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        Thread.sleep(1000); // Wait 1 second
        emailField.sendKeys(TEST_EMAIL);
        Thread.sleep(1000); // Wait 1 second
        passwordField.sendKeys(TEST_PASSWORD);
        loginButton.click();

        // Ensure successful login before proceeding
        wait.until(ExpectedConditions.urlContains("/home"));

        // Click the logout button
        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/logout']")));
        logoutLink.click();
        System.out.println("Clicked logout link");

        // Verify redirection to the login page after logout
        boolean logoutSuccess = wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(logoutSuccess, "Logout failed, did not redirect to login");
    }

    @Order(6)
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAccessToProtectedPage() {
        driver.get(BASE_URL + "/home");

        // Verify redirection to the login page after try to access oritected page
        boolean logoutSuccess = wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(logoutSuccess, "Protected page failed, did not redirect to login");

        System.out.println("Access to protected page denied");
    }
}
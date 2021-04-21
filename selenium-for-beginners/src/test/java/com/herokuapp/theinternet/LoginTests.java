package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class LoginTests {

	private WebDriver driver;

	@BeforeMethod(alwaysRun=true)

	private void setup() {
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		driver = new ChromeDriver();

		// adding sleep for 3 sec
		sleep(1000);

		// Maximize Browser window
		driver.manage().window().maximize();

	}

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })

	public void loginTest() {

		System.out.println("Starting Login Test");

		// Open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is open");

		sleep(1000);
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");
		sleep(1000);

		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");
		sleep(1000);

		WebElement loginButton = driver.findElement(By.tagName("button"));
		loginButton.click();
		sleep(1000);

		// Verification
		String expectedURL = "http://the-internet.herokuapp.com/secure";
		String actualURL = driver.getCurrentUrl();

		Assert.assertEquals(actualURL, expectedURL, "Actual page url is mot the same as expected");

		WebElement logoutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		Assert.assertTrue(logoutButton.isDisplayed(), "Logout button is not visible");

		WebElement successMessage = driver.findElement(By.cssSelector("div#flash"));
		String expectedMsg = "You logged into a secure area!";
		String actualMsg = successMessage.getText();
		// Assert.assertEquals(actualMsg, expectedMsg, "Actual Message is not the same
		// as expected");

		Assert.assertTrue(actualMsg.contains(expectedMsg), "Actual Message doen not contain expected");

		

	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String expectedErrorMessage) {
		System.out.println("Starting negativeLoginTest with " + username + " and " + password);

//		open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened.");

//		enter username
		WebElement usernameElement = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);

//		enter password
		WebElement passwordElement = driver.findElement(By.name("password"));
		passwordElement.sendKeys(password);

//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		sleep(3000);

		// Verifications
		WebElement errorMessage = driver.findElement(By.id("flash"));
		String actualErrorMessage = errorMessage.getText();

		Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),
				"Actual error message does not contain expected. \nActual: " + actualErrorMessage + "\nExpected: "
						+ expectedErrorMessage);

		
	}

	
	

	private void sleep(long s) {
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterMethod(alwaysRun=true)
	private void tearDown() {
		// Close browser
		driver.quit();
	}
}

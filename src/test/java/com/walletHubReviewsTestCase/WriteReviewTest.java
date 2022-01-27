package com.walletHubReviewsTestCase;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.automation.base.CommonActions;

public class WriteReviewTest extends CommonActions 
{
	public static WebDriver driver;

	@BeforeClass
	public void pageFactoryInitialization()
	{
		//Initialize page objects
		intializePageFactory(driver,"com.walletHubReviewsPageObjects.Locators");
	}

	@Parameters({"Environment","Browser"})
	@BeforeTest
	public void beforeTest(String environment,String browser) throws Exception
	{    	   		
		//Driver initialization
		driver = getDriver(browser);
		driver.get(appURL(environment));
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
	}

	@Parameters({"Environment","Browser"})
	@Test(priority = 0,groups = "WalletHub - Login account",testName = "Login to wallet hub account",description="Verify login wallethub account")
	public void loginUser(String environment,String browser) throws Exception
	{
		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.loginLinkString, driver, "Wait for login link to be visible");
		click(com.walletHubReviewsPageObjects.Locators.loginLink, driver, "Click on login link");
		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.emailIDTextBoxString, driver, "Wait for login modal to be visble");
		inputbox(com.walletHubReviewsPageObjects.Locators.emailIDTextBox,getEmaildId(environment), driver, "Enter email id");
		inputbox(com.walletHubReviewsPageObjects.Locators.passwordTextBox,getPassword(environment), driver, "Enter password");
		click(com.walletHubReviewsPageObjects.Locators.loginButton, driver, "Click on login button");
		driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
		driver.get("https://wallethub.com/profile/13732055i");
	}

	@Parameters({"Environment","Browser"})
	@Test(dependsOnMethods = {"loginUser"},priority = 1,groups = "WalletHub - Write review",testName = "Write new review on wallet hub",description="Verify new review added to wallethub")
	public void writeReview(String environment,String browser) throws Exception
	{
		//Soft assertion variable
		SoftAssert softAssertion = new SoftAssert();

		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.reviewLinkString, driver, "Wait until page is visible after login account");
		click(com.walletHubReviewsPageObjects.Locators.reviewLink, driver, "Click on review link");
		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.fourStarReviewString, driver, "Wait for review stars are visible");
		Thread.sleep(4000);
		click(com.walletHubReviewsPageObjects.Locators.fourStarReview, driver, "Click on fourth star");
		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.reviewDropDownOptionString, driver, "Wait for review page is visible");
		click(com.walletHubReviewsPageObjects.Locators.reviewDropDownOption, driver, "Click on dropdown to select health insurance option");
		click(com.walletHubReviewsPageObjects.Locators.healthInsuranceOption, driver, "Select health insurance option");
		keyBoardTabAction(driver, "Press tab key", softAssertion);
		keyboardActions(driver, getInputData(environment,"reviewText"), "Enter review", softAssertion);
		click(com.walletHubReviewsPageObjects.Locators.submitButton, driver, "Click on submit button");
		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.submitReviewString, driver, "Wait for review until text is visible");
		textContains(com.walletHubReviewsPageObjects.Locators.submitReviewText,getAssertionData("reviewText"), driver, "Assert review is submitted", softAssertion);
		click(com.walletHubReviewsPageObjects.Locators.continueButton, driver,"Click on continue button");
		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.reviewLinkString, driver, "Wait for user dashboard to be visible after review is added");
		click(com.walletHubReviewsPageObjects.Locators.reviewLink, driver, "Click on review link");
		waitForElementVisible(com.walletHubReviewsPageObjects.Locators.fourStarReviewString, driver, "Wait for review stars are visible");
		Thread.sleep(4000);
		click(com.walletHubReviewsPageObjects.Locators.mostRecentFilterLink, driver, "Click on most recent");
        click(com.walletHubReviewsPageObjects.Locators.mostRecentLink, driver, "Select most recent option");
        waitForElementVisible(com.walletHubReviewsPageObjects.Locators.userEmailIDString, driver, "Wait for my review is visible");
        elementvisible(com.walletHubReviewsPageObjects.Locators.userEmailID, driver, "Assert new comment added by user", softAssertion);

		//Collect all soft assertions if any
		softAssertion.assertAll();
	}
    
	@Parameters({"Environment","Browser"})
	@AfterMethod
	public void afterMethod(ITestResult arg,String environment,String browser) throws IOException
	{ 			  
		captureConsoleMessages(driver,"\""+arg.getMethod().getDescription()+"\"", browser); 	
	}

	@AfterTest
	public void afterTest() 
	{
		driver.manage().deleteAllCookies();
		driver.close();
		driver.quit();	
	}
}
package com.automation.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.extentReports.ExtentTestManager;

public class CommonActions extends BaseTest
{
	//Failed screenshot variable
	public static String failedScreenshotsPath = absoluteFilePath(fileSeperator+"FailedScreenshots").getAbsolutePath();

	//Wait time variables used in Explicit wait
	public static final int WAIT_TIME = 400;
	public static final int WAIT_TIME_INVISIBILITY_OF_ELEMENT = 400;
	public static final int WAIT_TIME_MILLISECONDS = 200;

	//Convert image to base64 string (Used in extent reports to display image in report)
	public static String imageToBase64String(String filePath)
	{
		String base64 = "";

		try
		{
			InputStream iSteamReader = new FileInputStream(filePath);
			byte[] imageBytes = IOUtils.toByteArray(iSteamReader);
			base64 = Base64.getEncoder().encodeToString(imageBytes);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return base64;
	}

	//Take screenshot
	public static void takeScreenshot(String testCaseName,WebDriver tempDriver) throws Exception 
	{
		try 
		{	
			File file = new File(failedScreenshotsPath+File.separator+testCaseName+".png");
			String temp_path = file.getAbsolutePath();

			TakesScreenshot scrShot =((TakesScreenshot)tempDriver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			File DestFile = new File(temp_path);
			FileUtils.copyFile(SrcFile, DestFile);

			String imgstr = imageToBase64String(DestFile.getAbsolutePath());
			ExtentTestManager.getTest().info("Click on below button to expand screenshot: ",MediaEntityBuilder.createScreenCaptureFromBase64String(imgstr).build());
		} 
		catch (Exception e)
		{
			System.out.println("Unable to takescreen shot");
			System.out.println(e.getMessage());
		}
	}

	//Fluent wait method
	public static void fluentWait(WebElement element,WebDriver tempDriver,String description) throws Exception
	{
		try
		{
			Wait<WebDriver> wait = new FluentWait<WebDriver>(tempDriver).withTimeout(Duration.ofSeconds(WAIT_TIME))
					.pollingEvery(Duration.ofMillis(WAIT_TIME_MILLISECONDS)).ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions.visibilityOf(element)); 

			if(!element.isDisplayed())
			{
				ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
				Assert.fail("Element "+element+" not visible");
			}
		}
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail("Wait for "+element+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot("Wait",tempDriver);
			Assert.fail("Element "+element+" not visible");
		}
	}

	//Wait for element visible using fluent wait
	public static void waitForElementVisible(String locator,WebDriver tempDriver,String description) throws Exception 
	{  
		try
		{
			Wait<WebDriver> wait = new FluentWait<WebDriver>(tempDriver).withTimeout(Duration.ofSeconds(WAIT_TIME))
					.pollingEvery(Duration.ofMillis(WAIT_TIME_MILLISECONDS)).ignoring(NoSuchElementException.class);
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator))); 

			if(!element.isDisplayed())
			{
				ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
				Assert.fail("Element "+element+" not visible");
			}
		}
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail("Wait for "+locator+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot("Wait",tempDriver);
			Assert.fail("Element "+locator+" not visible");
		}
	}

	//Wait for element invisibility
	public static void waitForElementNotVisible(String xpath,WebDriver tempDriver,String testCaseName) throws Exception
	{   

		try
		{
			WebDriverWait wait = new WebDriverWait(tempDriver,WAIT_TIME_INVISIBILITY_OF_ELEMENT);
			boolean test = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));

			if(test!=true)
			{
				ExtentTestManager.getTest().fail(testCaseName+". Please check below for errors. ");
				takeScreenshot(testCaseName,tempDriver);
				Assert.fail("Element with locator: "+xpath+" still visible");
			}
		}
		catch(TimeoutException e)
		{  
			ExtentTestManager.getTest().fail(testCaseName+". "+"Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			Assert.fail("Timeout to dissappear for element with locator "+xpath+" from webpage");
		}
	}

	//Highlight element
	public static void highlighElement(WebElement element,WebDriver tempDriver,String testCaseName)
	{
		JavascriptExecutor js = (JavascriptExecutor) tempDriver;
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
	}

	//Pressing tab key 
	public static void keyBoardTabAction(WebDriver tempDriver,String testCaseName,SoftAssert softAssert)
	{
		try
		{
			Actions a = new Actions(tempDriver);
			a.sendKeys(Keys.TAB).build().perform();
			ExtentTestManager.getTest().pass(testCaseName);   		  
		}
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail(testCaseName+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+"Keyboard tab failed"+"</span>");
			softAssert.fail("Keyboard tab failed"); 
		}
	}

	//Entering data using Actions
	public static void keyboardActions(WebDriver tempDriver,String key,String testCaseName,SoftAssert softAssert)
	{  	
		try
		{
			Actions a = new Actions(tempDriver);
			a.sendKeys(key).build().perform();
			ExtentTestManager.getTest().pass(testCaseName);
		}
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail(testCaseName+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+"Keyboard backspace failed"+"</span>");
			softAssert.fail("Keyboard backspace failed");
		}
	}

	//Method to type in textboxes
	public static void inputbox(WebElement element,String text,WebDriver tempDriver,String testCaseName) throws Exception 
	{
		try 
		{   
			fluentWait(element, tempDriver, testCaseName);
			element.sendKeys(text);
			ExtentTestManager.getTest().pass(testCaseName);
		} 
		catch (WebDriverException e) 
		{
			ExtentTestManager.getTest().fail(testCaseName+". "+"Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			Assert.fail("Element with locator "+element+" not found for input text");
		}
	}	

	//Assert textcontains method (element text verification)
	public static void textContains(WebElement element, String contains_value,WebDriver tempDriver,String testCaseName,SoftAssert softAssert) throws Exception
	{
		try
		{   	
			fluentWait(element, tempDriver, testCaseName);

			String actual = element.getText();

			if (actual.equalsIgnoreCase(contains_value))
			{			  
				ExtentTestManager.getTest().pass(testCaseName);
				ExtentTestManager.getTest().info("Actual text: "+"<span style='font-weight:bold;'>"+actual+"</span>" +" contains expected text "+"<span style='font-weight:bold;'>"+contains_value+"</span>");
			}
			else 
			{
				ExtentTestManager.getTest().fail(testCaseName+". Please check below for errors. ");
				ExtentTestManager.getTest().info("Actual text: "+"<span style='font-weight:bold;'>"+actual+"</span>" +" not contains expected text "+"<span style='font-weight:bold;'>"+contains_value+"</span>");
				takeScreenshot(testCaseName,tempDriver);
				softAssert.fail(testCaseName);
			}
		}
		catch(Exception e)
		{	
			ExtentTestManager.getTest().fail(testCaseName+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			softAssert.fail("Element with locator "+element+" not found to assert text");
		}
	}

	//Click on element
	public static void click(WebElement element,WebDriver tempDriver,String testCaseName) throws Exception
	{		 
		try
		{
			fluentWait(element, tempDriver, testCaseName);
			element.click();
			ExtentTestManager.getTest().pass(testCaseName);
		}
		catch (WebDriverException e)
		{			 	 
			ExtentTestManager.getTest().fail(testCaseName+". "+"Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			Assert.fail(element+" not found for click."+e.getMessage());
		}
	}

	//Mouseover on element
	public static void mousehover(WebElement element,WebDriver tempDriver,String testCaseName) throws Exception
	{ 	 
		try
		{     	
			fluentWait(element, tempDriver, testCaseName);

			Actions a = new Actions(tempDriver);
			a.moveToElement(element).build().perform();
			ExtentTestManager.getTest().pass(testCaseName); 		
		}
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail(testCaseName+". "+"Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			Assert.fail("Element with locator: "+element+" not visible for mouseover"); 
		}
	}

	//Assert if element is visible (Soft assertion)
	public static void elementvisible(WebElement element,WebDriver tempDriver,String testCaseName,SoftAssert softAssert) throws Exception
	{
		try
		{
			fluentWait(element, tempDriver, testCaseName);

			if(element.isDisplayed())
			{  
				ExtentTestManager.getTest().pass(testCaseName);
			}
		} 
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail(testCaseName+". "+"Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			softAssert.fail("Element with locator: "+element+" not visible");
		}
	} 

	//Assert if element is visible (Hard assertion)
	public static void elementvisible(WebElement element,WebDriver tempDriver,String testCaseName) throws Exception
	{
		try
		{
			fluentWait(element, tempDriver, testCaseName);

			if(element.isDisplayed())
			{  
				ExtentTestManager.getTest().pass(testCaseName);
			}
		} 
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail(testCaseName+". "+"Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			Assert.fail("Element with locator: "+element+" not visible");
		}
	} 

	//Assert elements visible method
	public static void elementsvisible(List<WebElement> element,WebDriver tempDriver,String testCaseName,SoftAssert softAssert) throws Exception
	{
		try
		{
			for(int w=0;w<element.size();w++)
			{
				if(element.get(w).isDisplayed())
				{  
					ExtentTestManager.getTest().pass(testCaseName);
				}
				else
				{
					ExtentTestManager.getTest().fail(element.get(w).toString()+" not visible. ");
					softAssert.fail(element.get(w).toString()+" not visible");
				}
			} 
		}
		catch(WebDriverException e)
		{
			ExtentTestManager.getTest().fail(testCaseName+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			softAssert.fail("Element with locator: "+element+" not visible to get all element");
		}
	}

	//Assert pagetitle method
	public static void pagetitle(String str,WebDriver tempDriver,String testCaseName,SoftAssert softAssert) throws Exception
	{
		String act = tempDriver.getTitle();

		if(act.equals(str))
		{
			ExtentTestManager.getTest().pass(testCaseName);
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>Title Matched</span>");		  
		}
		else
		{
			ExtentTestManager.getTest().fail(testCaseName+". Please check below for errors. ");
			ExtentTestManager.getTest().info("Title "+"<span style='font-weight:bold;'>"+act+"</span>"+" not Matched with "+"<span style='font-weight:bold;'>"+str+"</span>");
			takeScreenshot(testCaseName,tempDriver);
			softAssert.fail("Title not matched");
		}
	}    

	//Scroll down(Using Actions)
	public static void scrollPageDown(WebDriver tempDriver,JavascriptExecutor js,String description)
	{
		try
		{
			js = (JavascriptExecutor)tempDriver;
			js.executeScript("window.scrollBy(0,1000000000000)", "");
			ExtentTestManager.getTest().pass(description);
		}
		catch(JavascriptException e)
		{
			ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
		}	 
	}

	//Scroll down(Using for loop)
	public static void scrollPageDown2(WebDriver tempDriver,String description)
	{	 
		Actions a = new Actions(tempDriver);

		try
		{
			for(int i=0;i<=200;i++)
			{
				a.sendKeys(Keys.ARROW_DOWN).build().perform();
			} 

			ExtentTestManager.getTest().pass(description);
		}
		catch(Exception e)
		{
			ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
		} 
	}

	//Scroll page down according to the passed int count
	public static void scrollPageDown2(WebDriver tempDriver,int count,String description)
	{	 
		Actions a = new Actions(tempDriver);

		try
		{
			for(int i=0;i<=count;i++)
			{
				a.sendKeys(Keys.ARROW_DOWN).build().perform();
			} 

			ExtentTestManager.getTest().pass(description);
		}
		catch(Exception e)
		{
			ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
		} 
	}

	//Scroll up(Using Actions)
	public static void scrollPageUp(WebDriver tempDriver,JavascriptExecutor js,String description)
	{
		try
		{
			js = (JavascriptExecutor)tempDriver;
			js.executeScript("window.scrollBy(1000000000000,0)", "");
			ExtentTestManager.getTest().pass(description);
		}
		catch(JavascriptException e)
		{
			ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
		}	 
	}

	//Scroll up(Using for loop)
	public static void scrollPageUp2(WebDriver tempDriver,String description)
	{	 
		Actions a = new Actions(tempDriver);

		try
		{
			for(int i=0;i<=100;i++)
			{
				a.sendKeys(Keys.ARROW_UP).build().perform();
			} 
			ExtentTestManager.getTest().pass(description); 
		}
		catch(Exception e)
		{
			ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
		} 
	}

	//Scroll up to element
	public static void scrollPageToElement(WebDriver tempDriver,WebElement element,String description) throws Exception
	{	 
		try
		{
			Actions actions = new Actions(tempDriver);
			actions.moveToElement(element).build().perform();
			ExtentTestManager.getTest().pass(description);
		}
		catch(Exception e)
		{
			ExtentTestManager.getTest().fail(description+". Please check below for errors. ");
			ExtentTestManager.getTest().info("<span style='font-weight:bold;'>"+e.getMessage()+"</span>");
			takeScreenshot(description,tempDriver);
			Assert.fail("Element with XPATH: "+element+" not visible to scroll");
		}
	}

}
package com.automation.base;

import java.io.IOException;
import java.net.MalformedURLException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class BaseTest extends UtilityMethods
{
	//UI automation report and javascript console message file variables
	public static String automationReportName;
	public static String consoleMessagesReportName;
    
	@Parameters({"Environment","Browser"})
	@BeforeSuite
	public static void setUp(String environment,String browser) throws MalformedURLException, IOException
	{		
		//Check browsertype
		checkBrowser(browser);

		//Test case started statement
		System.out.println(" ");
		System.out.println("Starting UI "+System.getProperty("BuildVersion")+" Automation for "+environment+" on "+browser.toUpperCase());
		System.out.println("-------------------------------------------------------");

		//Clear console messages file
		clearTheFile();
	}
	
	@Parameters({"Environment","Browser"})
	@AfterSuite
	public static void tearDown(String environment,String browser)
	{
		//Test case ended statement
		System.out.println("-------------------------------------------------------");	
		System.out.println(" ");
		System.out.println("UI Automation completed on "+environment+" please refer report");
		System.out.println(" ");
	}
}
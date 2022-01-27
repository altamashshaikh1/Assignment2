package com.extentReports;

import java.util.HashMap;
import java.util.Map;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager
{
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
    static ExtentReports extent = ExtentManager.getInstance();
	public static ExtentTest test;
    
	//Get test name
	public static synchronized ExtentTest getTest() 
	{
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	//End test
	public static synchronized void endTest() 
	{
		extent.flush();
	}

	//Start test
	public static synchronized ExtentTest startTest(String testName)
	{
	    test = extent.createTest(testName);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		return test;
	}
}
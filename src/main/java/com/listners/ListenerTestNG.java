package com.listners;

import org.testng.ITestContext;		
import org.testng.ITestListener;		
import org.testng.ITestResult;
import com.aventstack.extentreports.Status;
import com.extentReports.ExtentManager;
import com.extentReports.ExtentTestManager;

public class ListenerTestNG extends ExtentTestManager implements ITestListener
{   	
	@Override
	public void onTestStart(ITestResult result)
	{
		ExtentTestManager.startTest(result.getMethod().getDescription());
		
		//Assign test category
		for (String group : result.getMethod().getGroups())
		{
			test.assignCategory(group);
		}	
	}

	@Override
	public void onTestSuccess(ITestResult Result)
	{
		System.out.println("");
		System.out.println(Result.getMethod().getDescription()+" : PASSED");
		System.out.println("");
	}

	@Override
	public void onTestFailure(ITestResult Result) 
	{
		System.out.println("");
		System.out.println(Result.getMethod().getDescription()+" : FAILED");
		System.out.println("");
	}

	@Override
	public void onTestSkipped(ITestResult Result) 
	{
		System.out.println("");
        ExtentTestManager.startTest(Result.getMethod().getDescription());
		
		//Assign test category
		for (String group : Result.getMethod().getGroups())
		{
			test.assignCategory(group);
		}	
		
		System.out.println(Result.getMethod().getDescription()+" : SKIPPED");
		ExtentTestManager.getTest().log(Status.SKIP, "TEST CASE SKIPPED DUE TO FAILURES IN PREVIOUS TESTS");
		System.out.println("");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onStart(ITestContext context) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onFinish(ITestContext context) 
	{
		ExtentTestManager.endTest();
		ExtentManager.getInstance().flush();
	}
}
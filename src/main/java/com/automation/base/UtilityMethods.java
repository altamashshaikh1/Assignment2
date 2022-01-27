package com.automation.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.PageFactory;
import com.extentReports.ExtentTestManager;

public class UtilityMethods
{
	//File seprator variable 
	public static String fileSeperator = System.getProperty("file.separator");

	//Check browser type
	public static void checkBrowser(String browserType) throws MalformedURLException, IOException
	{
		if(!browserType.equalsIgnoreCase("firefox")&&!browserType.equalsIgnoreCase("firefoxheadless")&&!browserType.equalsIgnoreCase("chrome")&&!browserType.equalsIgnoreCase("chromeheadless"))
		{
			System.out.println("");
			System.out.println(browserType+" not a valid browsertype.Please enter chrome, chromeheadless, firefox or firefoxheadless");
			System.exit(0);
		}
	}

	//Clear console error text file
	public static void clearTheFile() throws IOException 
	{
		FileWriter fwOb = new FileWriter(System.getProperty("user.dir")+fileSeperator+"ConsoleMessages"+fileSeperator+"allConsoleMessages.html", false); 
		PrintWriter pwOb = new PrintWriter(fwOb, false);
		pwOb.flush();
		pwOb.close();
		fwOb.close();
	}

	//Get input data
	public static String getInputData(String environment,String key)
	{
		String pvalue = null;

		switch(environment)
		{
		case "QAEnvironment" :
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"QAEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty(key);
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		case "BetaEnvironment" :
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"BetaEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty(key);
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		case "ProductionEnvironment" :
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"ProductionEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty(key);
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;
		}

		return pvalue;
	}

	//Get assertion data
	public static String getAssertionData(String key)
	{
		String pvalue = null;

		try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"AssertionData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
		{
			Properties properties = new Properties();
			properties.load(reader);
			pvalue = properties.getProperty(key);
		}
		catch (IOException e) 
		{
			System.out.println("Properties file not found to assert data : "+e.getMessage());
			ExtentTestManager.getTest().fail("Properties file not found to assert data : "+e.getMessage());
		}	

		return pvalue;
	}

	//Get data from ConfigurationFiles
	public static String getConfigData(String key)
	{
		String pvalue = null;
		try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"ConfigurationFiles"+fileSeperator+"run-configurations.properties").getAbsolutePath())) 
		{
			Properties properties = new Properties();
			properties.load(reader);
			pvalue = properties.getProperty(key);
		}
		catch (IOException e) 
		{
			System.out.println("Unable to find run-configurations.properties : "+e.getMessage());
			System.exit(0);
		}	
		return pvalue;	
	}

	//Method to return absolute file path based on environment
	public static File absoluteFilePath(String relativeFilePath)
	{
		File file = null; 

		if(getCurrentPlatform().contains("Windows"))
		{
			file = new File(System.getProperty("user.dir")+relativeFilePath);
		}
		else if(getCurrentPlatform().contains("nix")||getCurrentPlatform().contains("nux")||getCurrentPlatform().contains("aix"))
		{
			file = new File(System.getProperty("user.dir")+relativeFilePath);
		}
		else if(getCurrentPlatform().contains("mac"))
		{
			file = new File(System.getProperty("user.home")+relativeFilePath);
		}

		return file;
	}

	//Method to fetch current platform
	public static String getCurrentPlatform()
	{
		return System.getProperty("os.name");
	}

	//Page object initialization method
	public void intializePageFactory(WebDriver dr,String locatorClassName) 
	{  	 
		@SuppressWarnings("rawtypes")
		Class s;

		try 
		{
			s = Class.forName(locatorClassName);
			Object t = s.newInstance();
			PageFactory.initElements(dr,t);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Page objects intialization failed: "+e.getMessage());
		}  
	}

	//Method to resolve browser
	public static WebDriver getDriver(String type) throws Exception
	{
		switch (type.toLowerCase())
		{
		case  "firefox":
			Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);	
			String geckoDriverPath1 = absoluteFilePath(""+fileSeperator+"BrowserDrivers"+fileSeperator+"GeckoDriver.exe").getAbsolutePath();
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,System.getProperty("user.dir")+fileSeperator+"FirefoxDriverlogs.logs");
			System.setProperty("webdriver.gecko.driver", geckoDriverPath1);		
			FirefoxProfile profile1 = new FirefoxProfile();
			profile1.setPreference("browser.download.folderList", 2);
			profile1.setPreference("browser.download.manager.showWhenStarting", false);
			profile1.setPreference("browser.download.dir",absoluteFilePath(fileSeperator+"DownloadedFiles"+fileSeperator+"AuditLogs.json").getAbsolutePath());
			profile1.setPreference("browser.helperApps.neverAsk.openFile","text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
			profile1.setPreference("browser.helperApps.neverAsk.saveToDisk","text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
			profile1.setPreference("browser.helperApps.alwaysAsk.force", false);
			profile1.setPreference("browser.download.manager.alertOnEXEOpen", false);
			profile1.setPreference("browser.download.manager.focusWhenStarting", false);
			profile1.setPreference("browser.download.manager.useWindow", false);
			profile1.setPreference("browser.download.manager.showAlertOnComplete", false);
			profile1.setPreference("browser.download.manager.closeWhenDone", false);		
			FirefoxOptions ff2 = new FirefoxOptions();
			return new FirefoxDriver(ff2);

		case   "firefoxheadless":
			Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);	
			String geckoDriverPath2 = absoluteFilePath(""+fileSeperator+"BrowserDrivers"+fileSeperator+"GeckoDriver.exe").getAbsolutePath();
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,System.getProperty("user.dir")+fileSeperator+"FirefoxDriverlogs.logs");
			System.setProperty("webdriver.gecko.driver", geckoDriverPath2);		
			FirefoxProfile profile2 = new FirefoxProfile();
			profile2.setPreference("browser.download.folderList", 2);
			profile2.setPreference("browser.download.manager.showWhenStarting", false);
			profile2.setPreference("browser.download.dir",absoluteFilePath(fileSeperator+"DownloadedFiles"+fileSeperator+"AuditLogs.json").getAbsolutePath());
			profile2.setPreference("browser.helperApps.neverAsk.openFile","text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
			profile2.setPreference("browser.helperApps.neverAsk.saveToDisk","text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
			profile2.setPreference("browser.helperApps.alwaysAsk.force", false);
			profile2.setPreference("browser.download.manager.alertOnEXEOpen", false);
			profile2.setPreference("browser.download.manager.focusWhenStarting", false);
			profile2.setPreference("browser.download.manager.useWindow", false);
			profile2.setPreference("browser.download.manager.showAlertOnComplete", false);
			profile2.setPreference("browser.download.manager.closeWhenDone", false);		
			FirefoxOptions ff3 = new FirefoxOptions();
			ff3.addArguments("-headless");
			return new FirefoxDriver(ff3);

		case   "chrome":
			Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
			System.setProperty("webdriver.chrome.driver",absoluteFilePath(fileSeperator+"BrowserDrivers"+fileSeperator+"ChromeDriver.exe").getAbsolutePath());	
			System.setProperty("webdriver.chrome.args", "--disable-logging");
			System.setProperty("webdriver.chrome.silentOutput", "true");				
			Map<String, Object> prefs = new HashMap<String, Object>();		
			prefs.put("download.default_directory",absoluteFilePath(fileSeperator+"DownloadedFiles"+fileSeperator+"AuditLogs.json").getAbsolutePath());
			ChromeOptions c1 = new ChromeOptions();
			c1.setExperimentalOption("prefs", prefs);
			c1.addArguments("--window-size=1920,1080");
			c1.addArguments("--incognito");
			c1.addArguments("--disable-extensions");
			c1.addArguments("--log-level=3");
			c1.addArguments("--silent");
			c1.addArguments("--disable-infobars");			   
			if(getCurrentPlatform().contains("Windows"))
			{
				c1.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
			}
			return new ChromeDriver(c1);

		case   "chromeheadless":
			Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
			System.setProperty("webdriver.chrome.driver", absoluteFilePath(fileSeperator+"BrowserDrivers"+fileSeperator+"ChromeDriver.exe").getAbsolutePath());
			System.setProperty("webdriver.chrome.args", "--disable-logging");
			System.setProperty("webdriver.chrome.silentOutput", "true");
			Map<String, Object> prefs2 = new HashMap<String, Object>();
			prefs2.put("download.default_directory",absoluteFilePath(""+fileSeperator+"DownloadedFiles"+fileSeperator+"AuditLogs.json").getAbsolutePath());
			ChromeOptions c2 = new ChromeOptions();
			c2.setExperimentalOption("prefs", prefs2);
			c2.addArguments("headless");
			c2.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080","--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");
			c2.addArguments("--window-size=1920,1080");
			c2.addArguments("--incognito");
			c2.addArguments("--disable-extensions");
			c2.addArguments("--log-level=3");
			c2.addArguments("--silent");
			c2.addArguments("--disable-infobars");
			if(getCurrentPlatform().contains("Windows"))
			{
				c2.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
			}
			return new ChromeDriver(c2);

		default: throw new Exception("Invalid driver type " + type + "!");
		}
	}  

	//Capture all JS console messages
	public static void captureConsoleMessages(WebDriver tempDriver,String testCaseName,String browser) throws IOException
	{   
		if(!browser.equalsIgnoreCase("chrome")&&!browser.equalsIgnoreCase("chromeheadless"))
		{
			clearTheFile();
			try
			{
				//File file = new File(System.getProperty("user.dir")+fileSeperator+"ConsoleMessages"+fileSeperator+"allConsoleMessages.html");
				File file = new File(absoluteFilePath(fileSeperator+"ConsoleMessages"+fileSeperator+"allConsoleMessages.html").getAbsolutePath());
				FileWriter fileWrite = new FileWriter(file, true);
				BufferedWriter bufferedWrite = new BufferedWriter(fileWrite);

				//Create printwriter object
				PrintWriter write = new PrintWriter(bufferedWrite);
				file.createNewFile();
				write.flush();
				write.println("<br>");
				write.println("JAVASCRIPT CONSOLE LOGGING IS SUPPORTED ONLY FOR CHROME BROWSER");
				write.close();
			}
			catch(Exception e)
			{
				System.out.println("Unable to write console error messages in file");
			}
		}
		else
		{
			try
			{
				//Collect console errors if any
				LogEntries logEntries = tempDriver.manage().logs().get(LogType.BROWSER);

				if(logEntries.getAll().size()!=0)
				{
					//Create file
					//File file = new File(System.getProperty("user.dir")+"/ConsoleMessages/allConsoleMessages.html");
					File file = new File(absoluteFilePath(fileSeperator+"ConsoleMessages"+fileSeperator+"allConsoleMessages.html").getAbsolutePath());
					FileWriter fileWrite = new FileWriter(file, true);
					BufferedWriter bufferedWrite = new BufferedWriter(fileWrite);

					//Create printwriter object
					PrintWriter write = new PrintWriter(bufferedWrite);
					file.createNewFile();
					write.flush();
					write.println("<TABLE border=1 width = 100%><TR><TH bgcolor="+"#644987"+"  width="+"5%"+"><font color="+"white"+">Log Level</font></TH><TH bgcolor="+"#644987"+" width="+"95%"+"><font color="+"white"+">Console messages after executing "+testCaseName+" test case</font></TH></TR>");

					for (LogEntry entry : logEntries) 
					{
						write.println("<TR><TD Align=Center><PRE>"+entry.getLevel()+"</PRE></TD><TD><PRE>"+entry.getMessage()+"</PRE></TD></Tr>");  
					}

					write.println("</TABLE><br><hr><br>");
					write.close(); 
				}
				else
				{
					//File file = new File(System.getProperty("user.dir")+"/ConsoleMessages/allConsoleMessages.html");
					File file = new File(absoluteFilePath(fileSeperator+"ConsoleMessages"+fileSeperator+"allConsoleMessages.html").getAbsolutePath());
					FileWriter fileWrite = new FileWriter(file, true);
					BufferedWriter bufferedWrite = new BufferedWriter(fileWrite);

					//Create printwriter object
					PrintWriter write = new PrintWriter(bufferedWrite);
					file.createNewFile();
					write.flush();
					write.println("<TABLE border=1 width = 100%><TR><TH bgcolor="+"#644987"+" width="+"5%"+"><font color="+"white"+">Log Level</font></TH><TH bgcolor="+"#644987"+" width="+"95%"+"><font color="+"white"+">Console messages after executing "+testCaseName+" test case</font></TH></TR>");
					write.println("<TR><TD Align=Center colspan="+"2"+"><PRE>NO CONSOLE MESSAGES FOUND</PRE></TD></Tr>");
					write.println("</TABLE><br><hr><br>");
					write.close();
				}
			}
			catch(Exception e)
			{
				System.out.println("Unable to write console error messages in file");
			}  
		}
	}

	//Return todays date
	public static String todaysDate()
	{
		DateFormat currentDate;
		Calendar currentMonth;
		DateFormat currentYear;
		String date;

		currentDate = new SimpleDateFormat("dd");
		currentMonth = Calendar.getInstance();
		currentYear = new SimpleDateFormat("yyyy");
		date = currentDate.format(new Date())+new SimpleDateFormat("MMM").format(currentMonth.getTime())+currentYear.format(new Date()).toString();

		return date.toString();
	}

	//Set AUT URL
	public static String appURL(String environment)
	{
		String applicationURL = null;

		switch(environment)
		{
		case "QAEnvironment" :
			applicationURL = "<QA_URL>";
			break;

		case "BetaEnvironment" :
			applicationURL = "<BETA_URL>";
			break;

		case "ProductionEnvironment" :
			applicationURL = "https://wallethub.com";
			break;

		default : System.out.println(environment+" not a valid environment.");	
		}

		return applicationURL;
	}

	//Get input data (For login credentials)
	public static String getEmaildId(String environment)
	{
		String pvalue = null;

		switch(environment)
		{
		case "QAEnvironment" :   
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"QAEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty("userID");
				System.out.print(pvalue);
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		case "BetaEnvironment" :
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"BetaEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty("userID");
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		case "ProductionEnvironment" :
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"ProductionEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty("userID");
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		default : System.out.println(environment+" not a valid environment.");	
		}

		return pvalue;
	}

	//Get input data (For login credentials)
	public static String getPassword(String environment)
	{
		String pvalue = null;

		switch(environment)
		{
		case "QAEnvironment" :   
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"QAEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty("userPassword");
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		case "BetaEnvironment" :
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"BetaEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty("userPassword");
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		case "ProductionEnvironment" :
			try (FileReader reader = new FileReader(absoluteFilePath(fileSeperator+"InputData"+fileSeperator+"ProductionEnvironmentData"+fileSeperator+"Data.properties").getAbsoluteFile())) 
			{
				Properties properties = new Properties();
				properties.load(reader);
				pvalue = properties.getProperty("userPassword");
			}
			catch (IOException e) 
			{
				System.out.println("Properties file not found to input data : "+e.getMessage());
				ExtentTestManager.getTest().fail("Properties file not found to input data : "+e.getMessage());
			}	
			break;

		default : System.out.println(environment+" not a valid environment.");	
		}

		return pvalue;
	}
}
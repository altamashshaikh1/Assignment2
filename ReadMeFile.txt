Below are the components used in framework

1) Apache Maven v3.6.3
2) TestNG v6.14.3
3) TestNG listner 
4) Selenium 3.141.59
5) Extent reports v3.1.5
6) Page object and page factory to initialize the object model
7) Properties file to store input and assertion data

----------------------------------------------------------------------------------------------------------------------------------------------------

Folder structure

1)Source folder src/main/java


2)Source folder src/test/java


3)Source folder src/pageObjects/java

----------------------------------------------------------------------------------------------------------------------------------------------------

1)Folder contents

  - AssertionData : Consists of "Data.properties" file for assertion data
  - BrowserDrivers : Consists of .exe files for browser drivers
  - ConfigurationFiles : Consists of "run-configurations.properties" file which stores all configuration data like version number for tools used, domain name used AUT URL
  - ConsoleMessages : Consists of .html file to store the console messages post automation execution
  - InputData : Consists of "Data.properties" file for input data required in automation suite
  
----------------------------------------------------------------------------------------------------------------------------------------------------
  
Command to execute suite from terminal

         mvn test -DEnvironment=*ENVIRONMENTNAME* -DBrowserName=*BROWSERNAME* -DBuildVersion=*BUILDVERSION*
 
         - *ENVIRONMENTNAME* = BetaServer , ProductionServer
         - *BROWSERNAME* = chrome , chromeheadless, firefox, firefoxheadless
         - *BUILDVERSION* = v1.0
   
 For Eg : mvn test -DEnvironment=ProductionEnvironment -DBrowser=chromeheadless -DBuildVersion=v1.0
   
----------------------------------------------------------------------------------------------------------------------------------------------------
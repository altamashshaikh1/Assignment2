package com.walletHubReviewsPageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Locators 
{
	public static String loginLinkString = "//a[@class='login']";

	@FindBy(xpath = "//a[@class='login']")
	public static WebElement loginLink;

	public static String emailIDTextBoxString = "//input[@id='email']";

	@FindBy(id = "email")
	public static WebElement emailIDTextBox;

	@FindBy(id = "password")
	public static WebElement passwordTextBox;

	@FindBy(xpath = "//span[text()='Login']")
	public static WebElement loginButton;
	
	public static String reviewLinkString = "//span[text()='Reviews']";
	
	@FindBy(xpath = "//span[text()='Reviews']")
	public static WebElement reviewLink;
	
	public static String divModalString = "//div[starts-with(@class, 'rv review-action')]";
	
	public static String fourStarReviewString = "//*[@aria-label='4 star rating' and @role='radio']";

	@FindBy(xpath = "//*[@aria-label='4 star rating' and @role='radio']")
	public static WebElement fourStarReview;
	
	public static String reviewDropDownOptionString = "//span[text()='Select...']";
	
	@FindBy(xpath = "//span[text()='Select...']")
	public static WebElement reviewDropDownOption;
	
	@FindBy(xpath = "//*[text()='Health Insurance']")
	public static WebElement healthInsuranceOption;
	
	@FindBy(xpath = "//div[text()='Submit']")
	public static WebElement submitButton;
	
	public static String submitReviewString = "//*[text()='Your review has been posted.']";
	
	@FindBy(xpath = "//div[text()='Submit']")
	public static WebElement submitReviewText;
	
	@FindBy(xpath = "//div[text()='Continue']")
	public static WebElement continueButton;
	
	@FindBy(xpath = "//span[@class='af-icon-down-open']")
	public static WebElement mostRecentFilterLink;
	
	@FindBy(xpath = "//span[text()='Most Recent']")
	public static WebElement mostRecentLink;
	
	public static String userEmailIDString = "//span[text()=' Your Review']";
	
	@FindBy(xpath = "//span[text()=' Your Review']")
	public static WebElement userEmailID;
}
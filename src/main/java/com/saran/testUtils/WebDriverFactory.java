package com.saran.testUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;


public class WebDriverFactory {
	
	

	public static RemoteWebDriver getDriver(String browser) {
		WebDriver driver = null;
		
		switch(browser) {
		
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			options.addArguments("--start-maximized");
			//options.addArguments("--start-fullscreen");
			options.setHeadless(false);
			System.setProperty("webdriver.chrome.driver","./src/resources/Drivers/chromedriver.exe");
			driver =new ChromeDriver(options);
			break;
			
		case "firefox":
			System.setProperty("webdriver.gecko.driver","./src/resources/Drivers/geckodriver.exe");
			driver = new FirefoxDriver();//geckodriver
			break;
			
	/*	case "Htmlunit":
			driver = new HtmlUnitDriver();
			break;*/
			
		case "internetExplore":
			System.setProperty("webdriver.ie.driver","./src/main/resources/Drivers/IEdriver.exe");
			break;
			
		
		
		}
		return (RemoteWebDriver)driver;
		
	}


}

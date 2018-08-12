package com.saran.testUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.saran.reporting.HtmlReporting;
import io.appium.java_client.android.AndroidDriver;
import com.saran.testdataAccess.MSExcelReader;

public class ReuseableLibrary extends Utility {
	


	public ReuseableLibrary(MSExcelReader dataTable, AndroidDriver androiddriver, ExtentTest test) {
		super(dataTable, androiddriver, test);
		// TODO Auto-generated constructor stub
	}


	public ReuseableLibrary(MSExcelReader dataTable, WebDriver driver, ExtentTest test) {
		super(dataTable, driver, test);
		// TODO Auto-generated constructor stub
	}
	
	
	public void testmethods() {
		
		
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.MILLISECONDS);
		
		driver.manage().deleteAllCookies();
		
		
		WebDriverWait wait= new WebDriverWait(driver,20);
		
		WebElement test= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("")));
		
		
		
	}



}

package com.saran.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import com.saran. testdataAccess.MSExcelReader;
import com.saran.testUtils.ReuseableLibrary;
import com.saran.testUtils.Utility;


public class LoginPageMobile extends Utility {





	public LoginPageMobile(MSExcelReader dataTable, AndroidDriver androiddriver, ExtentTest test) {
		super(dataTable, androiddriver, test);

	}
	
	
	public void login() throws InterruptedException {

		androiddriver.findElement(By.id("username")).sendKeys("catsadm");
		androiddriver.findElement(By.id("password")).sendKeys("catscats11");
		androiddriver.findElement(By.id("btn_connect")).click();

}
}
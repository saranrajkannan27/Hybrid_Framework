package com.saran.businessComponents;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.saran.pages.LoginPage;
import com.saran. testdataAccess.MSExcelReader;
import com.saran.testUtils.TestParameters;
import com.saran.testUtils.Utility;


public class FunctionalComponentsWeb extends Utility {
	

	public FunctionalComponentsWeb(MSExcelReader dataTable,WebDriver driver,ExtentTest test) {
		super(dataTable,driver,test);
	}

	public void login() throws InterruptedException {
		LoginPage log = new LoginPage(dataTable, driver, test);
		log.login();
	

	}
	

}

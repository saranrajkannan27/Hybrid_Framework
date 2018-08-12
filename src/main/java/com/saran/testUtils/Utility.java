package com.saran.testUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.saran.reporting.HtmlReporting;
import io.appium.java_client.android.AndroidDriver;
import com.saran.testdataAccess.MSExcelReader;

public class Utility {

	public WebDriver driver;
	public MSExcelReader dataTable;
	public static Properties properties;
	public AndroidDriver androiddriver;
	public ExtentTest test;



	public Utility( MSExcelReader dataTable,WebDriver driver,ExtentTest test) {
		this.driver = driver;
		this.dataTable = dataTable;
		this.test=test;
	}

	public Utility( MSExcelReader dataTable,AndroidDriver androiddriver,ExtentTest test) {
		this.androiddriver = androiddriver;
		this.dataTable = dataTable;
		this.test=test;
	}

	public Utility() {

	}
	public void setPropeties(Properties properties) {
		this.properties = properties;
	}


	public static String getCurrentFormattedTime(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}
	
	public String screenShot() {
		TakesScreenshot scr;
		if(properties.getProperty("ExecutionType").equalsIgnoreCase("Mobile")) {
			scr= ((TakesScreenshot)androiddriver);
		}
		else {
			scr= ((TakesScreenshot)driver);
		}
		String screenshootname= getCurrentFormattedTime("dd_MMM_yyyy_hh_mm_ss_SSS");

		File scrfile=scr.getScreenshotAs(OutputType.FILE);

		File destfile= new File("./Results/"+HtmlReporting.reportFolderName()+"/"+screenshootname+".png");

		try {
			FileUtils.copyFile(scrfile,destfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return screenshootname;

	}

	
	public void addInfo(LogStatus status, String testInfo) {
		
		test.log(status,"<b>"+ testInfo+"</b>", "");
		
		
	}
	
	
	public void addSteps(LogStatus status, String testInfo ) {
		
		test.log(status,testInfo,"<b>Screenshot:</b>"+test.addScreenCapture("./" + screenShot()+ ".png"));
		
	}

	
}

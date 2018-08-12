package com.saran.reporting;

import java.io.File;
import com.relevantcodes.extentreports.ExtentReports;

import com.relevantcodes.extentreports.NetworkMode;

import com.saran.testUtils.Utility;

public class HtmlReporting extends Utility {
	
	
	private static String reportFolderName="RUN_"+getCurrentFormattedTime("dd_MMM_yyyy_hh_mm");
	private static String relativePath=new File (System.getProperty("user.dir")).getAbsolutePath();
	public static String reportpath=relativePath+"\\Results\\"+reportFolderName+"\\TestSummary.html";
	
	// Create an object of HtmlReport
	private static ExtentReports Instance= new ExtentReports(reportpath,true,NetworkMode.OFFLINE);
	
	// Create constructor private so that this class cannot be instantiated
	private HtmlReporting() {
		
	}
	
	// Get the only object available
	
	public static ExtentReports getInstance() {
		return Instance;
		
	}

	public static String reportFolderName() {
		
		
		return reportFolderName;
		
	}

	

}

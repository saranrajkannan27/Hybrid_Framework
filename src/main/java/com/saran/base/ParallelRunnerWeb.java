package com.saran.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.codepine.api.testrail.TestRailException;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.saran.testUtils.PlatformFactory;
import com.saran.testUtils.TestParameters;
import com.saran.testUtils.TestRailListener;
import com.saran.testUtils.Utility;
import com.saran.testUtils.WebDriverFactory;
import com.saran. testdataAccess.MSExcelReader;



public class ParallelRunnerWeb extends Utility implements Runnable {

	//private Workbook dataTable;
	private TestParameters testParameters;
	private String testStatus;
	private MSExcelReader dataTable;
	private WebDriver driver;
	private ExtentReports report;
	private ExtentTest test;
	String testRailEnabled=testRailProperties.getProperty("testRail.enabled");
	private TestRailListener testRailListenter;

	public ParallelRunnerWeb(TestParameters testParameters,MSExcelReader dataTable,ExtentReports report, TestRailListener testRailListenter) {

		this.dataTable = dataTable;
		this.testParameters = testParameters;
		this.report = report;
		this.testRailListenter=testRailListenter;
	}

	@Override
	public void run() {


		try {
			intializeWebDriver();
			if(testParameters.getExecuteCurrentTestcase().equalsIgnoreCase("Yes")) {
				test = report.startTest(testParameters.getCurrentTestcase()+" : "+ testParameters.getDescription());
			}
			test.log(LogStatus.INFO, testParameters.getCurrentTestcase()+" execution started","");
			invokeTestScript(getKeywords());			
			test.log(LogStatus.INFO, testParameters.getCurrentTestcase()+" execution completed","");
			report.endTest(test);
			report.flush();
			testRailReport();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			report.flush();
			testRailReport();
		} catch (InstantiationException e) {
			e.printStackTrace();
			report.flush();
			testRailReport();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			report.flush();
			testRailReport();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			report.flush();
			testRailReport();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			report.flush();
			testRailReport();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			report.flush();
			testRailReport();
		} catch (SecurityException e) {
			e.printStackTrace();
			report.flush();
			testRailReport();
		}


	}



	public LinkedHashMap<String, String>getKeywords(){
		
		LinkedHashMap<String,String> keywordMap = new LinkedHashMap<String,String>();
		keywordMap= dataTable.getRowData("BusinessComponents",testParameters.getCurrentTestcase());
		return keywordMap;

	}


	private  void intializeWebDriver() {

		String gridmode= properties.getProperty("GridMode");
		String gridURL= properties.getProperty("GridHubURL");

		if(gridmode.equalsIgnoreCase("ON")) {

			DesiredCapabilities capability= new DesiredCapabilities();
			capability.setBrowserName(testParameters.getBrowser());
			capability.setPlatform(testParameters.getPlatform());


			try {
				driver = new RemoteWebDriver(new URL(gridURL), capability);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
		else {
			driver = WebDriverFactory.getDriver(testParameters.getBrowser());
		}

	}

	private void invokeTestScript(LinkedHashMap<String, String> keywords) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class<?> className = Class.forName("com.saran.businessComponents.FunctionalComponentsWeb");
		Constructor<?> constructor = className.getDeclaredConstructors()[0];
		Object classInstance = constructor.newInstance(dataTable,driver,test);

		for (Entry<String, String> map : keywords.entrySet()) {

			if(!map.getKey().equals("TC_ID")) {
				String currentKeyword = map.getValue().substring(0,1).toLowerCase() + map.getValue().substring(1);
				test.log(LogStatus.INFO, "<font size=2 face = Bedrock color=blue><b>"+currentKeyword.toUpperCase()+"</font></b>" );
				Method method = className.getMethod(currentKeyword);
				method.invoke(classInstance);
			}

		}
		
		end();

	}


	private void testRailReport() {
		if( testRailEnabled.equalsIgnoreCase("true")) {
			try {

				if(test.getRunStatus()==LogStatus.PASS ) {

					int testRunID=Integer.parseInt(testRailProperties.getProperty("testRail.runId"));
					int testcaseID=Integer.parseInt( testParameters.getTestRailId());

					testRailListenter.addTestResult(testRunID,testcaseID,1);
					test.log(LogStatus.INFO, "Result for Test case with ID <b>" +  testParameters.getTestRailId() + "</b> is <b>PASSED</b> in TestRail");
				}else {

					testRailListenter.addTestResult(Integer.parseInt(testRailProperties.getProperty("testRail.runId")),Integer.parseInt( testParameters.getTestRailId()),5);
					test.log(LogStatus.INFO, "Result for Test case with ID <b>" +  testParameters.getTestRailId() + "</b> is <b>PASSED</b> in TestRail");

				}
			}
			catch(TestRailException e) {

				if(e.getResponseCode()==400) {
					test.log(LogStatus.FAIL, "TestRail not updated for the testcase "+testParameters.getCurrentTestcase()+"  with testrail ID "+testParameters.getTestRailId());
					test.log(LogStatus.INFO, e);
				}

			}

		}
		
		
		
	}
	public void end() {

		if(driver!=null) {
			driver.quit();	
		}

	}
}
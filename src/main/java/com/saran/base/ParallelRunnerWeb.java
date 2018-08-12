package com.saran.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.saran. testdataAccess.MSExcelReader;
import com.saran.testUtils.TestParameters;
import com.saran.testUtils.WebDriverFactory;



public class ParallelRunnerWeb implements Runnable {

	//private Workbook dataTable;
	private TestParameters testParameters;
	private String testStatus;
	private MSExcelReader dataTable;
	private WebDriver driver;
	private ExtentReports report;
	private ExtentTest test;

	public ParallelRunnerWeb(TestParameters testParameters,MSExcelReader dataTable,ExtentReports report) {

		this.dataTable = dataTable;
		this.testParameters = testParameters;
		this.report = report;
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

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	public LinkedHashMap<String, String>getKeywords(){
		LinkedHashMap<String,String> keywordMap = new LinkedHashMap<String,String>();
		keywordMap= dataTable.getRowData("BusinessComponents",testParameters.getCurrentTestcase());
		return keywordMap;

	}


	private  void intializeWebDriver() {

		driver = WebDriverFactory.getDriver(testParameters.getBrowser());
		//driver.manage().window().maximize();

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



	public void end() {

		if(driver!=null) {
			driver.quit();	
		}

	}
}
package com.saran.base;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import com.saran. testdataAccess.MSExcelReader;
import com.saran.testUtils.TestParameters;


public class ParallelRunnerMobile implements Runnable {
	
	private TestParameters testParameters;
	private MSExcelReader dataTable;
	private AndroidDriver androiddriver;
	private ExtentReports report;
	private ExtentTest test;
	private AppiumServiceBuilder builder;
	private AppiumDriverLocalService service;
	private String serviceurl;
	
	
	public ParallelRunnerMobile(TestParameters testparameters, MSExcelReader dataTable, ExtentReports report) {
		
		this.testParameters = testparameters;
		this.dataTable = dataTable;
		this.report = report;

	}
	
	
	public void run() {
		
		try {
			//appiumServerSetup();
			try {
				androidDriverSetup();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			if(testParameters.getExecuteCurrentTestcase().equalsIgnoreCase("Yes")) {
				test = report.startTest(testParameters.getCurrentTestcase()+" : "+ testParameters.getDescription());
			}
			test.log(LogStatus.INFO, testParameters.getCurrentTestcase()+" execution started");
			invokeTestScript(getKeywords());			
			test.log(LogStatus.INFO, testParameters.getCurrentTestcase()+" execution completed");
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
	
	private void invokeTestScript(LinkedHashMap<String, String> keywords) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Class<?> className = Class.forName("com.saran.businessComponents.FunctionalComponentsMobile");
		Constructor<?> constructor = className.getDeclaredConstructors()[0];
		Object classInstance = constructor.newInstance(dataTable,androiddriver,test);

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

		if(androiddriver!=null) {
			androiddriver.quit();	
		}

	}

	
	public String appiumServerSetup() {
		
		builder=new AppiumServiceBuilder();
		builder.usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"));
		builder.withAppiumJS(new File("C:\\Users\\Saran\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"));
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(4723);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"debug");	
		service = AppiumDriverLocalService.buildService(builder);
		service.start();
		serviceurl=service.getUrl().toString();
		System.out.println(serviceurl);
		return serviceurl;
		
	}
	
	public void androidDriverSetup() throws MalformedURLException {
		String absolutePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		serviceurl=appiumServerSetup();
		DesiredCapabilities capabilities=new DesiredCapabilities();	
		
		capabilities.setCapability("BROWSER_NAME", "Android");
		capabilities.setCapability("VERSION", "4.4.2");
		capabilities.setCapability("deviceName", "emulator-5554");
		//capabilities.setCapability("app", absolutePath + "\\src\\main\\resources\\CATS-Mobility.apk");	
		capabilities.setCapability("appPackage","net.fulcrum.mobility" );
		capabilities.setCapability("appActivity","net.fulcrum.mobility.activities.LoginActivity" );
		capabilities.setCapability("unicodeKeyboard","true" );
		capabilities.setCapability("resetKeyboard","true" );
		capabilities.setCapability("noReset","true" );
		capabilities.setCapability("newCommandTimeout", "15000");
		androiddriver=new AndroidDriver<AndroidElement> (new URL(serviceurl),capabilities);	
	}
}

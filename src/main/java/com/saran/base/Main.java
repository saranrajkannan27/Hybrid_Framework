package com.saran.base;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentReports;

import com.saran.reporting.*;
import com.saran. testdataAccess.MSExcelReader;
import com.saran.testUtils.FrameworkProperties;
import com.saran.testUtils.TestParameters;
import com.saran.testUtils.TestRailListener;
import com.saran.testUtils.Utility;
import com.saran.testUtils.WebDriverFactory;

/**
 * @author Saran
 * This the main method.
 * 
 */

public class Main {

	private static String absolutepath;
	private static Properties properties;
	private static Properties testRailproperties;
	private static FrameworkProperties globalproperties;
	private static FrameworkProperties frameworkTestRailProperties;
	private static Workbook runManager;
	private static LinkedList<TestParameters> testInstancetoRun;
	private static Workbook dataTable;
	private static MSExcelReader runMangerPath;
	private static MSExcelReader dataTablePath;
	private static TestParameters testParameters;
	private static ExtentReports report;
	
	private static String propertyFilePath = "./src/resources/Properties/GlobalProperties.properties";
	private static String testRailpropertyFilePath = "./src/resources/Properties/TestRailProperties.properties";
	private static TestRailListener testRailListener;

	public  static void main(String[] args) {
		
		prepare();
		System.out.println("prepare");
		intializeTestReport();
		System.out.println("intializeTestReport");
		intializeTestRailReporting();
		collectRuninfo();
		System.out.println("collectRuninfo");
		intializeDataTable();
		System.out.println("intializeDataTable");
		execute();
		System.out.println("execute");
		warp();
		System.out.println("warp");
		
	

	}
	


	private static ExtentReports intializeTestReport() {
		report = HtmlReporting.getInstance();
		report.loadConfig(new File("./src/resources/Properties/extent-report-config.xml"));
		report.addSystemInfo("Project", properties.getProperty("Project"));
		report.addSystemInfo("Environment", properties.getProperty("Environment"));		
		
		return report;
	}


   private static void intializeTestRailReporting() {
	   
	  if( testRailproperties.getProperty("testRail.enabled").equalsIgnoreCase("true")) {
		  
		  int projectId= Integer.parseInt(testRailproperties.getProperty("testRail.projectId"));
		  testRailListener= new TestRailListener(projectId);
		  testRailListener.intialize();
		  
		  if(testRailproperties.getProperty("testRail.addNewRun").equalsIgnoreCase("True")) {
			  testRailListener.addTestRun();
		  }  
	  }
   }
	/**
	 * prepare method is used to get framework properties.
	 */
	private static void prepare() {
		
		setAbsolutepath();
		collectGlobalProperties();
		collectTestRailProperties();
	
	}
	




	/**
	 * absolutepath method is used to get path of framework current folder .
	 */
	
	private static void setAbsolutepath() {
		
		absolutepath = new File(System.getProperty("user.dir")).getAbsolutePath();
					
	}
	
	/**
	 * setAbsolutepath method is used to get framework parameters from property file.
	 */
	
	private static void collectGlobalProperties() {
		
		globalproperties = FrameworkProperties.getInstance();
		
		properties=	globalproperties.loadPropertyFile(propertyFilePath );
		
		Utility utils=new Utility();		
		utils.setPropeties(properties);
	}
	
	
	private static void collectTestRailProperties() {
		
		frameworkTestRailProperties = FrameworkProperties.getInstance();
		
		testRailproperties=	frameworkTestRailProperties.loadPropertyFile(testRailpropertyFilePath );
		
		Utility utils=new Utility();		
		utils.setTestRailPropeties(testRailproperties);
	}
	
	
	private static void collectRuninfo() {
		
		String testSuite = properties.getProperty("TestSuite");
		
		runMangerPath=MSExcelReader.getInstance();
		runManager=runMangerPath.createobjectWorkbook("./RunManager.xls");
		testInstancetoRun = runMangerPath.getRunMangerInfo(testSuite);
		
	}

	private static void intializeDataTable() {
		String testdataName = properties.getProperty("TestDataTableName");
		dataTablePath=MSExcelReader.getInstance();
		dataTable = dataTablePath.createobjectWorkbook("./src/resources/TestData/"+testdataName);
	
	}

	
	private static void execute() {

		int nThreads = Integer.parseInt(properties.getProperty("NumberOfThreads"));
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThreads);
		Runnable testRunner = null;

		for(int currentTestInstance=0 ;currentTestInstance<testInstancetoRun.size();currentTestInstance++) {
			
			String executiontype=properties.getProperty("ExecutionType");
			
			if(executiontype.equalsIgnoreCase("Webapp")){
			testRunner = new ParallelRunnerWeb(testInstancetoRun.get(currentTestInstance), MSExcelReader.getInstance(), report,testRailListener);
			parallelExecutor.execute(testRunner);
			}
			if(executiontype.equalsIgnoreCase("Mobile")) {
			testRunner = new ParallelRunnerMobile(testInstancetoRun.get(currentTestInstance), MSExcelReader.getInstance(), report);
			parallelExecutor.execute(testRunner);	
				
			}

		}

		parallelExecutor.shutdown();

		while(!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void warp() {
	
		report.flush();
		copyResulttoFolder();
		
		try {
			Desktop.getDesktop().open(new File(HtmlReporting.reportpath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	public static void copyResulttoFolder() {
		
		emptyLatestResultFolder();
			try {
			FileUtils.copyDirectory(new File("./Results/"+HtmlReporting.reportFolderName()), new File("./LatestResult"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	private static void emptyLatestResultFolder(){
		File directory = new File("./LatestResult");
		// make sure directory exists
		if (!directory.exists()) {
			// Do Nothing
		} else {
			try {
				delete(directory);
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

	}

	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}
		} else {
			// if file, then delete it
			file.delete();
		}
	}

	
}

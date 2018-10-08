

package com.saran.pages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import com.saran. testdataAccess.MSExcelReader;
import com.saran.testUtils.ReuseableLibrary;



public class LoginPage extends ReuseableLibrary {





	public LoginPage(MSExcelReader dataTable, WebDriver driver, ExtentTest test) {
		super(dataTable, driver, test);

	}
	
	
	public void login() {
		
	
		//driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		driver.get("https://www.google.in/");
		
		test.log(LogStatus.PASS, "Launched Google");
		addSteps(LogStatus.PASS, "Launched the site");
		
		
	 //driver.navigate().to("https://portal.bsnl.in/myportal/");
	//	javascriptEntertext();
	//	javascriptClick();
	//	isclickable();
	}

	/**
	 * JavascriptExecutor js= (JavascriptExecutor)driver;
	 * 
	 * entertext
	 * js.executeScript("document.getElementById('id of the element').value='saran';");
	 * 
	 * click
	 * js.executeScript("document.getElementById('id of the element').click();");
	 * 
	 * unselect checkbox
	 * js.executeScript("document.getElementById('id of the element').checked=false;")
	 * 
	 * 
	 * 
	 */

     public void isclickable() {
    	 
    	 driver.get("https://www.google.in/");
    	 
    	
    	 if(driver.findElements(By.xpath("//input[@value='Google Search1']")).size()!=0	){

    		 System.out.println("Element is present");
    		 WebElement element= driver.findElement(By.xpath("//input[@value='Google Search1']"));
    		 if(element.isEnabled()&&element.isDisplayed()) {
    			 System.out.println("Element is clickable");
    		 }else {
    			 System.out.println("Element is not clickable");
    		 }

    	 }else {
    		 
    		 System.out.println("Element not  is present");
    		 
    	 }
     }
	public  void javascriptClick() {
		//driver.get("https://www.google.in/");
		
		WebElement ele = driver.findElement(By.xpath("//input[@value='Google Search']"));
		
		JavascriptExecutor js= (JavascriptExecutor)driver;
		//js.executeScript("arguments[0].click();", ele);
		js.executeScript("document.getElementById('').click()");//if we have id we can use this
		
	}


	public void javascriptEntertext() {
		

		driver.get("https://www.google.in/");
		
		JavascriptExecutor js= (JavascriptExecutor)driver;
		js.executeScript("document.getElementById('lst-ib').value='Javascript';");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	public void verifyBrokenImage() {
		
		
		driver.get("https://www.amazon.in/");
		
		List<WebElement> list = driver.findElements(By.tagName("img"));		
		System.out.println("Total no of Links :"+list.size());

		for(int i=0; i<list.size();i++) {

			WebElement link= list.get(i);

			String url= link.getAttribute("src");

			try {	
				URL linkurl= new URL(url);

				HttpURLConnection httpcon= (HttpURLConnection)linkurl.openConnection();

				httpcon.setConnectTimeout(3000);

				httpcon.connect();

				if(httpcon.getResponseCode()==200){

					System.out.println(url+"--"+httpcon.getResponseMessage());

				}

				if(httpcon.getResponseCode()==httpcon.HTTP_NOT_FOUND) {

					System.out.println(url+"--"+httpcon.getResponseMessage()+"--"+httpcon.HTTP_NOT_FOUND);
				}

			}

			catch(Exception e) {

			}

			addInfo(LogStatus.PASS, "Launched the site");

			addSteps(LogStatus.PASS, "Launched the site");


		}
		
	}
	
	public void verifyBrokenLink() {
		
		driver.get("https://www.amazon.in/");
		

		
		List<WebElement> list= driver.findElements(By.tagName("a"));


		System.out.println("Total Links :" +list.size());

		for(int i=0; i<list.size();i++) {

			WebElement link= list.get(i);

			String url=link.getAttribute("href");


			try {
				URL urllink=new URL(url);

				try {
					HttpURLConnection httpcon=(HttpURLConnection)urllink.openConnection();
					httpcon.setConnectTimeout(2000);
					httpcon.connect();
					 System.out.println(urllink+" - "+httpcon.getResponseMessage());


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}




			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
	}

}
}
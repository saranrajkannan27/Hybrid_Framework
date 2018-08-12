package com.saran.testUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 
 * @author Saran
 * Java Singleton Design pattern
 *
 */


public class FrameworkProperties {


	//create an object of FrameworkProperties
	private static FrameworkProperties instance = new FrameworkProperties();


	//make the constructor private so that this class cannot be instantiated  
	private FrameworkProperties() {

	}

	//Get the only object available
	public static FrameworkProperties getInstance() {
		return instance;
	}

	public Properties loadPropertyFile(String propertyFilePath) {
		Properties properties = new Properties();
		
		try (FileInputStream input =new FileInputStream(propertyFilePath);){
			 
			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
						
		return properties;
	}

}

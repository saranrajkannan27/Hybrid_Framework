package com.saran.testUtils;

import org.openqa.selenium.Platform;

public class PlatformFactory {
	

	public static Platform getPlatform(String platformName) {
		
		Platform platform = null;
		
		if(platformName.equalsIgnoreCase("Windows")) {
			
			platform=Platform.WINDOWS;
			
		}
		else if(platformName.equalsIgnoreCase("Linux")){
			
			platform=Platform.LINUX;
			
		}
		else if(platformName.equalsIgnoreCase("Mac")) {
			
			platform=Platform.MAC;
		}

		return platform;
		
	}
	
	
	
	
	
	

}

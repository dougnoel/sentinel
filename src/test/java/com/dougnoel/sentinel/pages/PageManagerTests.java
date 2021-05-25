package com.dougnoel.sentinel.pages;

import java.util.Iterator;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class PageManagerTests {
	
	private static WebDriver driver;
	private static Page pageElement = null;
	private static String baseUrl = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = WebDriverFactory.instantiateWebDriver();
		pageElement = PageManager.setPage("SampleTestPage");
        baseUrl = Configuration.url();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		try {
	        Set<String> windows = driver.getWindowHandles();
	        Iterator<String> iter = windows.iterator();
	        String[] winNames = new String[windows.size()];
	        int i = 0;
	        while (iter.hasNext()) {
	            winNames[i] = iter.next();
	            i++;
	        }
	        if(winNames.length > 1) {
	            for(i = winNames.length; i > 1; i--) {
	                driver.switchTo().window(winNames[i - 1]);
	                driver.close();
	            }
	        }
	        driver.switchTo().window(winNames[0]);
	        driver.close();
	    }
	    catch(Exception e){         
	        e.printStackTrace();
	    }
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.NoSuchWindowException.class)
	public void getWindowHandleInSameWindow() {
		PageManager.switchToNewWindow();
	}
	
	@Test
	public void getWindowHandleInNewWindow() {
		PageManager.openPage(baseUrl);
		pageElement.getElement("open_new_window").click();
		PageManager.switchToNewWindow();
	}
	
	@Test
	public void getWindowHandleInNewTab() {
		PageManager.openPage(baseUrl);
		pageElement.getElement("open_new_tab").click();
		PageManager.switchToNewWindow();
	}
}

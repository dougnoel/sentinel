package com.dougnoel.sentinel.webdrivers;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.framework.PageManager;
import com.dougnoel.sentinel.pages.Page;

public class SentinelDriver {
	//The webdriver
	private WebDriver driver;
	//The windows  for the driver
	private WindowList windows;
	//The page objects used in order
	private List<Page> pages = new LinkedList<>();
	//Current page we are on

	SentinelDriver(WebDriver driver) {
		this.driver = driver;
		windows = new WindowList(driver);
		pages.add(PageManager.getPage());
	}
	
	//Find out if this driver has been used for a particular page object
	
	//close window
	protected void close() {
		windows.closeCurrentWindow();
	}
}
package com.dougnoel.sentinel.pages;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.webdrivers.Driver;

/**
 * The Page Manager is a singleton class that manages what page the test is on.
 * Calling setPage with a string containing the name of the new page calls the
 * Page Factory to create the new page (if it does not exist) and return it as 
 * a Page Object.
 */
public class PageManager {
	private static final Logger log = LogManager.getLogger(PageManager.class);
	// Only one page reference should exist. We aren't doing multi-threading.
	private static Page page = null;
	// The type of the current page object so that if we are creating a page object that doesn't contain
	// URLs or Executables, we can infer the current page type from the previous one.
	private static PageObjectType pageObjectType = PageObjectType.UNKNOWN;

	private static WebDriver driver() { return Driver.getWebDriver(); }

	private PageManager() {
		// Exists only to defeat instantiation.
	}

	/**
	 * This method sets a Page Object based on the class name passed to it. This
	 * allows us to operate on pages without knowing they exist when we write step
	 * definitions.
	 *
	 * @param pageName String Must be an exact string match (including case) to the Page Object name (e.g. LoginPage).
	 */
	public static void setPage(String pageName) {
		try {
			PageManager.page = PageFactory.buildOrRetrievePage(pageName);
			pageObjectType = PageManager.getPage().getPageObjectType();
			page.clearTables();
		} catch (NullPointerException npe) {
			page = null;
		}
	}

	/**
	 * This method returns the current Page Object stored in the Page Manager.
	 *
	 * @return Page the Page Object
	 */
	public static Page getPage() {
		if (page == null)
			throw new NotFoundException("Page not created yet. It must be navigated to before it can be used.");
		return page;
	}
	
	/**
	 * Creates a WebDriver if it doesn't exist and opens up the webpage or application.
	 *
	 * @param pageName String the name of the page object to open
	 * @param arguments String the arguments to pass as a query string if this is a web page
	 */
	public static void open(String pageName, String arguments) {
    	PageManager.setPage(pageName);
    	if (pageObjectType == PageObjectType.WEBPAGE) {
	    	String url = Configuration.url();
	    	url += arguments == null ? "" : arguments;
	    	log.debug("Loading the the {} page using the url: {}", pageName, url);
	    	driver().get(url);
    	}
    	else
    		driver();
	}

	/**
	 * Sets page load timeout on web driver instance using the timeout and timeunit values set in
	 * the configuration file or on the command line. Then interfaces with isPageLoaded to continually
	 * test if a page is loaded until it returns true or times out.
	 *
	 * @see PageManager#isPageLoaded()
	 *
	 * @return boolean always returns true, will throw exception if page does not load
	 * @throws InterruptedException if the thread gets interrupted
	 */
	public static boolean waitForPageLoad() throws InterruptedException {
		if(PageManager.getPage().getPageObjectType() != PageObjectType.EXECUTABLE) {
			driver().manage().timeouts().pageLoadTimeout(Time.out().toSeconds(), TimeUnit.SECONDS);
			while (!isPageLoaded()) {
				Thread.sleep(20);
			}
		}
		return true;
	}

	/**
	 * Returns true if document.readyState is complete for the current non-windows-application driver,
	 * meaning the page has loaded successfully. Uses the driver's pageLoadTimeout
	 * setting to throw a TimeoutException if the body element cannot be found on
	 * the page.
	 *
	 * @return boolean true if page has loaded or the page is a windows application, false if not.
	 */
	private static boolean isPageLoaded() {
		try {
			// TimeoutException is triggered by using driver().findElement
			driver().findElement(By.tagName("body"));
		} catch (TimeoutException e) {
			throw new TimeoutException(
					"This page timed out before it could finish loading. Please increase the timeout, ensure the page you are loading exists, or check your internet connection and try agin.");
		}
		// if we've gotten this far, we haven't timed out so return the
		// document.readyState check
		return ((JavascriptExecutor) driver()).executeScript("return document.readyState").equals("complete");
	}

	/**
	 * Returns what the page Manager believes to be the current page object type.
	 * Will return WEBPAGE, EXECUTABLE, or UNKNOWN if we don't know.
	 * This value is set every time a new webpage or executable is opened
	 * using the PageManager.open() method.
	 *
	 * @return PageObjectType the type of page we are on
	 */
	public static PageObjectType getCurrentPageObjectType() {
		return pageObjectType;
	}
	
}
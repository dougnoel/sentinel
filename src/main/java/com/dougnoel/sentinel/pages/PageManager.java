package com.dougnoel.sentinel.pages;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * The Page Manager is a singleton class that manages what page the test is on.
 * Calling setPage with a strong containing the name of the new page calls the
 * Page Factory to create the new page and return it as a Page Object.
 */
public class PageManager {
	private static final Logger log = LogManager.getLogger(PageManager.class);
	// Only one page reference should exist. We aren't doing multi-threading.
	private static Page page = null;
	// Only one page manager can exist.
	private static PageManager instance = null;
	// Page handle for the first window opened.
	private static String parentHandle = null;
	private static String parentPage = null;

	protected static WebDriver driver() { return WebDriverFactory.getWebDriver(); }

	private PageManager() {
		// Exists only to defeat instantiation.
	}

	/**
	 * This method sets a Page Object based on the class name passed to it. This
	 * allows us to operate on pages without knowing they exist when we write step
	 * definitions.
	 * <p>
	 * <b>NOTE:</b> This function is currently only intended for generic step
	 * definitions, as it makes the code complex to write otherwise.
	 * 
	 * @param pageName String Must be an exact string match (including case) to the Page Object name (e.g. LoginPage).
	 * @return Page Returns a reference to the page in case you want to use it immediately.
	 */
	public static Page setPage(String pageName) {
		// Ensure we only have one instance of this class, so that we always
		// return the same driver.
		if (instance == null)
			instance = new PageManager();

		// Get a page from the page factory
		PageManager.page = PageFactory.buildOrRetrievePage(pageName);
		return page;
	}

	/**
	 * This method returns the current Page Object stored in the Page Manager.
	 * 
	 * @return Page the Page Object
	 */
	public static Page getPage() {
		if (instance == null || page == null)
			throw new NotFoundException("Page not created yet. It must be navigated to before it can be used.");
		
		return page;
	}

	/**
	 * Open the URL passed, set the parent window handle and return it.
	 * 
	 * @param pageURL String url to open
	 * @return String WebDriver handle identifying string - unique for a window and
	 *         for each tab.
	 */
	public static String openPage(String pageURL) {
		open(pageURL);
		return driver().getWindowHandle();
	}

	/**
	 * Opens up the URL passed to it.
	 * <p>
	 * <b>TO DO:</b> Return a page object of the page we are on now.
	 * 
	 * @param url String Full URL to navigate to.
	 */
	protected static void open(String url) {
		try {
			driver().get(url);
		}
		/* Adding this to catch the case where a unit test breaks encapsulation by using the 
		 * WebDriver to directly close the browser instead of using the WebDriverManager to do it.
		 */
		catch (org.openqa.selenium.NoSuchSessionException e) {
			WebDriverFactory.instantiateWebDriver().get(url);
		}
	}


	/**
	 * Quits the current driver. Subsequent calls to the driver will fail. Should be
	 * used at the end of tests only.
	 * 
	 * @deprecated use WebDriverFactory.quit() instead.
	 */
	@Deprecated
	public static void quit() {
		WebDriverFactory.quit();
	}

	/**
	 * Emulate clicking the browser's forward button.
	 * <p>
	 * <b>TO DO:</b> We should be checking the URL to see if we are going to an
	 * existing page, and if so, passing that off as the new page object.
	 * 
	 * @return Page the current page object for chaining
	 */
	public static Page navigateForward() {
		driver().navigate().forward();
		return page;
	}

	/**
	 * Emulate clicking the browser's back button.
	 * <p>
	 * <b>TO DO:</b> We should be checking the URL to see if we are going to an
	 * existing page, and if so, passing that off as the new page object.
	 * 
	 * @return Page the current page object for chaining
	 */
	public static Page navigateBack() {
		driver().navigate().back();
		return page;
	}

	/**
	 * Emulate clicking the browser's refresh button.
	 * 
	 * @return Page the current page object for chaining
	 */
	public static Page refresh() {
		driver().navigate().refresh();
		return page;
	}

    /**
     * Maximizes the browser window. Stores the current window size and position so
     * you can return to the existing settings.
     * 
     * @return Page - Returns a page object for chaining.
     */
    public Page maximizeWindow() {
        driver().manage().window().maximize();
        return page;
    }

    /**
     * Returns the title of the current web page we are on. Useful for debugging and
     * assertions.
     * 
     * @return String
     */
    public static String getPageTitle() {
        return driver().getTitle();
    }
    
	/**
	 * Switches focus of the WebDriver to a new window assuming there was only one
	 * before. Finds the handle and passes that to overloaded switchToNewWindow()
	 * method
	 * <p>
	 * <b>Preconditions:</b> Expects a new tab or window to have just been opened,
	 * and for there to be only two.
	 * 
     * @param pageName the new page name to set the window to so it switches correctly
     * @return String the window handle we are switching to
	 * @throws InterruptedException if page doesn't load
     */
	public static String switchToNewWindow(String pageName) throws InterruptedException {
		String newHandle = null;
		parentHandle = driver().getWindowHandle();
		parentPage = PageManager.getPage().getName();
		Set<String> handles = driver().getWindowHandles();
		if (handles.size() == 1) {
			var errorMessage = "Only one window is open, therefore we cannot switch to a new window. Please open a new window and try again.";
			log.error(errorMessage);
			throw new NoSuchWindowException(errorMessage);
		}
		if (parentHandle == null) {
			var errorMessage = "Parent Window cannot be found. Please open a window and restart your test.";
			log.error(errorMessage);
			throw new NoSuchWindowException(errorMessage);
		}
		for (String handle : handles) {
			if (!handle.equals(parentHandle)) {
				newHandle = handle;
			}
		}
		switchToNewWindow(pageName, newHandle);
		return newHandle;
	}

	/**
	 * Overloads sentinel.pages.PageManager#switchToNewWindow() to
	 * accept a window index. Calls original
	 * sentinel.pages.PageManager#switchToNewWindow() and passes the
	 * index. This will allow for more fine grained control at a later date.
	 * 
	 * @param pageName the new page name to set the window to so it switches correctly
	 * @param index String the window to which we want to switch
	 * @throws InterruptedException if page doesn't load
	 */
	private static void switchToNewWindow(String pageName, String index) throws InterruptedException {
		try {
			driver().switchTo().window(index);
	        setPage(pageName);
	        waitForPageLoad();
			log.trace("Switched to new window {}", index);
		} catch (org.openqa.selenium.NoSuchWindowException e) {
			var errorMessage = SentinelStringUtils.format(
					"The expected window is already closed or cannot be found. Please check your intended target:  {}",
					e.getMessage());
			log.error(errorMessage);
			throw new NoSuchWindowException(errorMessage);
		}
	}

	/**
	 * Closes the child window and returns to using the parent window. Used for
	 * things like PDFs or links opening in a new tab or window that need to be
	 * closed after a test is complete.
	 * 
	 * @return String the handle of the parent window to which we are returning
	 * @throws InterruptedException if page does not load
	 */
	public static String closeChildWindow() throws InterruptedException {
		WebDriverFactory.close();
		return switchToParentWindow();
	}

	/**
	 * Switches to the parent window without closing the child window.
	 * 
	 * @return String the handle of the parent window to which we are returning
	 * @throws InterruptedException
	 */
	public static String switchToParentWindow() throws InterruptedException {
		driver().switchTo().window(parentHandle);
		setPage(parentPage);
		waitForPageLoad();
		return parentHandle;
	}

	/**
	 * Switches focus of the WebDriver to an iFrame.
	 */
	public static void switchToIFrame() {
		try {
			driver().switchTo().frame(0);
			log.trace("Switched to iFrame on current page");
		} catch (org.openqa.selenium.NoSuchFrameException e) {
			var errorMessage = SentinelStringUtils.format(
					"No iFrames were found on the current page. Ensure you have the correct page open, and please try again. {}",
					e.getMessage());
			log.error(errorMessage);
			throw new NoSuchFrameException(errorMessage);
		}

	}
	
	/**
	 * Exits existing iFrame.
	 */
	public static void exitIFrame() {
		driver().switchTo().defaultContent();
	}

	/**
	 * Gets the URL of the page we are currently on by calling the Page Object which
	 * has access to the driver object..
	 * 
	 * @return String the URL of currently active window
	 */
	public static String getCurrentUrl() {
		String currentUrl = null;
		try {
			currentUrl = driver().getCurrentUrl();
			log.trace("Current URL retrieved: {}", currentUrl);
		} catch (WebDriverException e) {
			var errorMessage = SentinelStringUtils.format(
					"An error occured when trying to find the current URL for {}. Please check the URL and try again: {}",
					page.getName(), e.getMessage());
			log.error(errorMessage);
			throw new NotFoundException(errorMessage);

		}
		if (currentUrl == null) {
			var errorMessage = "Current URL could not be found. Please check the URL and try again.";
			log.error(errorMessage);
			throw new NotFoundException(errorMessage);
		}
		return currentUrl;
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
		driver().manage().timeouts().pageLoadTimeout(Time.out().toSeconds(), TimeUnit.SECONDS);
		while (!isPageLoaded()) {
			Thread.sleep(20);
		}
		return true;
	}

	/**
	 * Returns true if document.readyState is complete for the current driver,
	 * meaning the page has loaded successfully. Uses the driver's pageLoadTimeout
	 * setting to throw a TimeoutException if the body element cannot be found on
	 * the page.
	 * 
	 * @return boolean true if page has loaded, false if not
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
		return ((JavascriptExecutor) driver()).executeScript("return document.readyState").equals("complete");
	}
}

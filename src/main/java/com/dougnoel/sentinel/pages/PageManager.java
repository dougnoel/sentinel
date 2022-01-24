package com.dougnoel.sentinel.pages;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
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
import org.openqa.selenium.support.ui.FluentWait;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.Driver;
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
	private static PageObjectType pageObjectType = PageObjectType.UNKNOWN;
	// Only one page manager can exist.
	private static PageManager instance = null;
	// Information regarding the previous page
	private static Pair<String, Pair<Page, String[]>> previousPageInfo = null;
	// Information regarding the current page
	private static Pair<String, Pair<Page, String[]>> currentPageInfo = null;
	// Information regarding all pages that have been utilized previously
	private static Map<String, Pair<Page, String[]>> pages = new ConcurrentHashMap<>();

	protected static WebDriver driver() { return Driver.getDriver(); }

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

		String[] pageInformation = windowScannerPruner();
		currentPageInfo = Pair.of(pageName, Pair.of(PageManager.page, pageInformation));

		pages.computeIfAbsent(currentPageInfo.getKey(), pageInfo -> currentPageInfo.getValue());
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
	 * Creates a WebDriver if it doesn't exist and opens up the webpage or application.
	 *
	 * @param pageName String the name of the page object to open
	 * @param arguments String the arguments to pass as a query string if this is a web page
	 */
	public static void open(String pageName, String arguments) {
    	PageManager.setPage(pageName);
    	pageObjectType = PageManager.getPage().getPageObjectType();
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
     * Switches to a previously utilized window which still exists
     * <p>
     * <b>Preconditions:</b> Only one page exists for each page object
     * @param pageName The name of the page to switch to
     * @return The window handle of the switched-to window
     * @throws NoSuchWindowException If no page of that name was utilized previously
     */
    public static String switchToExistingWindow(String pageName) {
    	previousPageInfo = currentPageInfo;

    	if(pages.containsKey(pageName)) {
    		driver().switchTo().window(pages.get(pageName).getRight()[0]);
    		setPage(pageName);
    	}
    	else {
    		throw new NoSuchWindowException("No page of that name was used previously");
    	}

    	return driver().getWindowHandle();
    }

    /**
     * Switches to the first found unvisited page and associates it with the page object
     * associated with the passed page name.
     * <p>
     * <b>Preconditions:</b> Only one new window should have opened.
     * @param pageName The page object to associate with the newly switched to page
     * @return The window handle of the switched-to window
     * @throws NoSuchWindowException if a new window cannot be switched to in the configured
     * timeout.
     */
    public static String switchToNewWindow(String pageName) {
    	previousPageInfo = currentPageInfo;
    	ArrayList<String> allKnownHandles = new ArrayList<>();
    	
    	for(Map.Entry<String, Pair<Page, String[]>> entry : pages.entrySet()) {
			allKnownHandles.add(entry.getValue().getRight()[0]);
		}

		try {
			FluentWait<WebDriver> wait = new FluentWait<>(driver())
				       .withTimeout(Time.out().plusSeconds(30))
				       .pollingEvery(Time.interval())
				       .ignoring(Exception.class);

			wait.until(d -> {
				var updatedWindowHandleList = driver().getWindowHandles();

				if(updatedWindowHandleList.isEmpty()) {
					return false;
				}

				for(String foundWindowHandle : updatedWindowHandleList) { 
					if(!allKnownHandles.contains(foundWindowHandle)) {
						driver().switchTo().window(foundWindowHandle);
						return true;
					}
				}

				return false;
			});
		}
		catch (TimeoutException e) {
			throw new NoSuchWindowException("Failed to switch to the new window");
		}

		setPage(pageName);
		return driver().getWindowHandle();
	}

    /**
     * Attempts to get the data on the current window, and prunes null or old tracked windows
     * which no longer exist from the history list based on their window handle + title
     * combination.
     * @return Returns the handle and title of the current window as a String array
     * <p>
     * If the handle and title cannot be retrieved
     * it will return a String array of [null,null]
     */
	private static String[] windowScannerPruner() {
		String currentHandle = null;
		String currentTitle = null;
		
		try {
			currentHandle = driver().getWindowHandle();
			currentTitle = driver().getTitle();
		}catch(Exception e) { 
			//Suppress errors if the window closes rapidly i.e. update windows
		}
		
		var updatedWindowHandleList = driver().getWindowHandles();

		for(Map.Entry<String, Pair<Page,String[]>> entry : pages.entrySet()) {
			String[] windowToCheck = entry.getValue().getRight();
			String windowPage = entry.getKey();
			
			if(!updatedWindowHandleList.contains(windowToCheck[0])) {
				pages.remove(windowPage);
			}
		}

		return new String[] {currentHandle, currentTitle};
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
		driver().switchTo().window(previousPageInfo.getValue().getRight()[0]);
        setPage(previousPageInfo.getKey());
        waitForPageLoad();
		return previousPageInfo.getValue().getRight()[0];
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

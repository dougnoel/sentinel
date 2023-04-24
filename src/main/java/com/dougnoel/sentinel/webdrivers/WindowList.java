package com.dougnoel.sentinel.webdrivers;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.util.*;

/**
 * Keeps track of all windows a driver has open and which one the user is currently on.
 * 
 * @author dougnoel@gmail.com
 */
public class WindowList {
	private List<String> windowHandles = new LinkedList<>();
	private int currentWindow = 0;
	private WebDriver driver;
	
	/**
	 * When a WindowList is created, you must pass it a reference to the driver that created it.
	 * This driver is stored and the list of windows is created and then maintained for it.
	 * Since this object is destroyed when the driver is destroyed, it should never have a stale reference to a driver.
	 * 
	 * @param driver WebDriver the containing driver object that is related to this driver object
	 */
	public WindowList(WebDriver driver) {
        this.driver = driver;
        addNewWindows();
        markCurrentWindow();
    }

	/**
	 * Checks for added or removed windows so the list is up-to-date.
	 */
	private void refreshWindows(){
		addNewWindows();
		pruneClosedWindows();
	}

	/**
	 * Search the driver for all windows and add any that don't exist.
	 *
	 * @return int Returns the number of added windows
	 */
	private int addNewWindows() {
		int numWindowsAdded = 0;
		Collection<String> windowsToAdd = new HashSet<>();

    	Set<String> currentWindows = driver.getWindowHandles();
    	for (String window : currentWindows) {
			if (!windowHandles.contains(window)) {
				numWindowsAdded += 1;
				windowsToAdd.add(window);
			}
		}

		windowHandles.addAll(windowsToAdd); //Prevents concurrency issue
		return numWindowsAdded;
	}
	
	/**
	 * Track the current window.
	 */
	private void markCurrentWindow() {
    	String curentWindowHandle = driver.getWindowHandle();
    	if (!curentWindowHandle.equals(windowHandles.get(currentWindow))) {
    		currentWindow = windowHandles.indexOf(curentWindowHandle);
    	}
	}

	/**
	 * If any windows have been closed, remove them from our list.
	 *
	 * @return int Returns the number of pruned windows
	 */
	private int pruneClosedWindows() {
		int numberOfWindowsPruned = 0;
		Set<String> currentWindows = driver.getWindowHandles();
		Collection<String> windowsToRemove = new HashSet<>();

    	for (String window : windowHandles) {
    		if (!currentWindows.contains(window)){
				numberOfWindowsPruned += 1;
				windowsToRemove.add(window);

				//If an earlier window than the current closes, decrement the window position
				if(windowHandles.indexOf(window) < currentWindow)
					currentWindow--;
			}
    	}

		windowHandles.removeAll(windowsToRemove); //Prevents concurrency issue
		return numberOfWindowsPruned;
	}

	/**
	 * Search the available window handles to find a window with the expected title. Searches until default timeout.
	 * @param title the title of the window to get the handle of
	 * @return the handle of the window that has the title expected
	 */
	private String getHandleFromTitle(String title, boolean contains) {
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); // fetch starting time

		while ((System.currentTimeMillis() - startTime) < searchTime) {
			refreshWindows();
			String handleWithMatchingTitle = null;
			if(contains){
				handleWithMatchingTitle = searchForWindowByTitleContains(title);
			}
			else{
				handleWithMatchingTitle = searchForWindowByTitleEquals(title);
			}
			if(handleWithMatchingTitle != null)
				return handleWithMatchingTitle;
		}

		String errorMessage = SentinelStringUtils.format("A window with the title {} was not found within the timeout of {} seconds.", title, Time.out().getSeconds());
		throw new NoSuchElementException(errorMessage);
	}

	/**
	 * Searches through all open windows to find one with a title that contains the given string.
	 * @param title String the title to search for in the windows.
	 * @return String the handle of the window with the given string in the title. Null if no window found with a matching title.
	 */
	private String searchForWindowByTitleContains(String title){
		for (String handle : windowHandles) {
			try {
				driver.switchTo().window(handle);
				if (driver.getTitle().contains(title))
					return handle;
			} catch (Exception e) {
				//Catch if the window changes while we're looking
			}
		}
		return null;
	}

	/**
	 * Searches through all open windows to find one with a title that equals the given string.
	 * @param title String the title to search for in the windows.
	 * @return String the handle of the window with the given string in the title. Null if no window found with a matching title.
	 */
	private String searchForWindowByTitleEquals(String title){
		for (String handle : windowHandles) {
			try {
				driver.switchTo().window(handle);
				if (driver.getTitle().equals(title))
					return handle;
			} catch (Exception e) {
				//Catch if the window changes while we're looking
			}
		}
		return null;
	}

	/**
	 * Wait for a new window to be added, and moves the driver to the last window added.
	 */
	protected void goToNewWindow() {
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); // fetch starting time

		while ((System.currentTimeMillis() - startTime) < searchTime) {
			int addedWindows = 0;
			try {
				addedWindows = addNewWindows();
			}
			catch (WebDriverException e) {
				//Suppress the generic web driver exception error as this can occur if 0 windows currently exist (waiting on new window with all previous windows closed)
			}

			if(addedWindows > 0){
				currentWindow = windowHandles.size()-1;
				driver.switchTo().window(windowHandles.get(currentWindow));
				pruneClosedWindows();
				return;
			}
		}

		String timeoutMessage = SentinelStringUtils.format("A new window/page/tab did not load in the configured new window timeout of {} seconds.", Time.out().getSeconds());
		throw new TimeoutException(timeoutMessage);
	}
	    
    /**
     * Closes the current window and moves the driver to the previous window. If no previous window exists,
     * we call close to clean up.
     */
    protected void closeCurrentWindow() {
    	driver.close();
		pruneClosedWindows();
    	try {
    		goToPreviousWindow();
    	} catch (NoSuchWindowException nswe) {
    		driver.quit();
    	}
    }
    
    /**
     * Moves the driver to the next window.
     */
    protected void goToNextWindow() {
		addNewWindows();
    	currentWindow++;
    	driver.switchTo().window(windowHandles.get(currentWindow));
		pruneClosedWindows();
    }
    
    /**
     * Moves the driver to the previous window.
     */
    protected void goToPreviousWindow() {
    	currentWindow--;
    	driver.switchTo().window(windowHandles.get(currentWindow));
    	pruneClosedWindows();
    }
    
    /**
     * Moves the driver to the last window in our list, which is presumably the most recent one created.
     */
    protected void goToLastWindow() {
    	addNewWindows();
    	currentWindow = windowHandles.size() - 1;
    	driver.switchTo().window(windowHandles.get(currentWindow));
    	pruneClosedWindows();
    }

	/**
	 * Switches to the window whose title matches the string passed
	 * @param title String the title of the window to go to
	 */
	protected void goToTitledWindow(String title) {
		refreshWindows();
		String windowHandle = getHandleFromTitle(title, false);
		driver.switchTo().window(windowHandle);
		currentWindow = windowHandles.indexOf(windowHandle);
	}

	/**
	 * Switches to the window whose title contains the string passed
	 * @param titleContains String the partial title window to search for and switch to
	 */
	protected void goToTitledWindowThatContains(String titleContains) {
		refreshWindows();
		String windowHandle = getHandleFromTitle(titleContains, true);
		driver.switchTo().window(windowHandle);
		currentWindow = windowHandles.indexOf(windowHandle);
	}

    /**
     * Empty out the list of windows.
     */
    protected void clear() {
    	windowHandles.clear();
    	currentWindow = 0;
    }

	/**
	 * Checks if a window of the passed title exists
	 * @param title String the title of the window to check for
	 * @return true if a window is found that has the title passed as the string
	 */
	protected boolean doesWindowExist(String title) {
		pruneClosedWindows();
		addNewWindows();
		for (String handle : windowHandles) {
			driver.switchTo().window(handle);
			if (driver.getTitle().equals(title))
				return true;
		}
		addNewWindows();
		return false;
	}
}
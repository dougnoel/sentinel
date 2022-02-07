package com.dougnoel.sentinel.webdrivers;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Keeps track of all windows a driver has open and which one the user is currently on.
 * 
 * @author dougnoel@gmail.com
 */
public class WindowList {
	private static final Logger log = LogManager.getLogger(WindowList.class);
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

	// Search the passed driver for all windows
	// If any don't exist in our current list, add them
	private void addNewWindows() {
    	Set<String> currentWindows = driver.getWindowHandles();
    	for (String window : currentWindows) {
    		if (!windowHandles.contains(window))
    			windowHandles.add(window);
    	}
	}
	
	//Track the current window
	private void markCurrentWindow() {
    	String curentWindowHandle = driver.getWindowHandle();
    	if (!curentWindowHandle.equals(windowHandles.get(currentWindow))) {
    		currentWindow = windowHandles.indexOf(curentWindowHandle);
    	}
	}

	//If any windows have been closed, remove them from our list.
	private void pruneClosedWindows() {
		Set<String> currentWindows = driver.getWindowHandles();
    	for (String window : windowHandles) {
    		if (!currentWindows.contains(window))
    			windowHandles.remove(window);
    	}
	}
    /**
     * Check to see if any windows have been created for the passed driver and if so, add them to the linked list.
     * We then get our current window from the driver and if it doesn't match where we are in our list, we update our position.
     */
    private void update() {
    	addNewWindows();
    	markCurrentWindow();
    	pruneClosedWindows();
    }
    
    public void goToWindow(WebDriver currentDriver, String windowHandle) {
    	//switch to the passed window
    	currentDriver.switchTo().window(windowHandle);
    	//run trackWindows
    	update();
    }
    
    /**
     * Closes the current window and moves the driver to the previous window. If no previous window exists,
     * we call close to clean up.
     */
    protected void closeCurrentWindow() {
    	//close window
    	//if no more windows exist, throw an exception
    	//set the iterator to the previous window in our list
    	//delete the old window in our list
    	//set the driver to the new window
    	//run trackWindows
    }
    
    protected void goToNextWindow() {
    	//if no window exists after the current one, throw an exception
    	//set the iterator to the next window in the list
    	//set the driver to the new window
    	//run trackWindows
    }
    
    protected void goToPreviousWindow() {
    	//if no window exists after the current one, throw an exception
    	//set the iterator to the next window in the list
    	//set the driver to the new window
    	//run trackWindows
    }
}

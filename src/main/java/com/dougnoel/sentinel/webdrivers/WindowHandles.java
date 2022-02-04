package com.dougnoel.sentinel.webdrivers;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Handles all windows for a driver.
 * 
 * @author dougnoel@gmail.com
 */
public class WindowHandles {
	private static final Logger log = LogManager.getLogger(Driver.class);
	private List<String> windowHandles = new LinkedList<>();
	private int currentWindow = 0;
	
	private WindowHandles() {
        // Exists only to defeat instantiation.
    }

    /**
     * Check to see if any windows have been created for the passed driver and if so, add them to the linked list.
     * We then get our current window from the driver and if it doesn't match where we are in our list, we update our position.
     */
    protected void trackWindows(WebDriver currentDriver) {
    	// Search the passed driver for all windows
    	// If any don't exist in our current list, add them
    	Set<String> currentWindows = currentDriver.getWindowHandles();
    	for (String window : currentWindows) {
    		if (!windowHandles.contains(window))
    			windowHandles.add(window);
    	}

    	//Track the current window
    	String curentWindowHandle = currentDriver.getWindowHandle();
    	if (curentWindowHandle != windowHandles.get(currentWindow)) {
    		currentWindow = windowHandles.indexOf(curentWindowHandle);
    	}
    	
    	//If any windows have been closed, remove them from our list.
    	for (String window : windowHandles) {
    		if (!currentWindows.contains(window))
    			windowHandles.remove(window);
    	}

    }
    
    protected void goToWindow(WebDriver currentDriver, String windowHandle) {
    	//switch to the passed window
    	currentDriver.switchTo().window(windowHandle);
    	//run trackWindows
    }
    
    /**
     * Closes the current window and moves the driver to the previous window. If no previous window exists,
     * we call close to clean up.
     */
    protected void close() {
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

package com.dougnoel.sentinel.webdrivers;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

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
	 * Search the driver for all windows and add any that don't exist.
	 */
	private void addNewWindows() {
    	Set<String> currentWindows = driver.getWindowHandles();
    	for (String window : currentWindows) {
    		if (!windowHandles.contains(window))
    			windowHandles.add(window);
    	}
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
	 */
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
    
    /**
     * 
     * @param windowHandle
     */
    protected void goToWindow(String windowHandle) {
    	driver.switchTo().window(windowHandle);
    	update();
    }
    
    /**
     * Closes the current window and moves the driver to the previous window. If no previous window exists,
     * we call close to clean up.
     */
    protected void closeCurrentWindow() {
    	driver.close();
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
     * Empty out the list of windows.
     */
    protected void clear() {
    	windowHandles.clear();
    	currentWindow = 0;
    }
}

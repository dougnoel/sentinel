package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.DownloadManager;
import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class WindowAndTabSteps {

    /**
     * Closes the currently active window or browser tab.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I close the current window"</li>
     * <li>I close the current tab"</li>
     * <li>I close the browser window</li>
     * <li>I close the browser tab</li>
     * </ul>
     */
    @When("^I close the (?:browser|current) (?:tab|window)$")
    public static void closeCurrentWindow() {
        Driver.closeWindow();
    }
    
    /**
     * Opens the given pageName in a new window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify a new tab opens the Home page</li>
     * <li>I verify a new window opens to the Login page</li>
     * <li>I verify a new tab opens to the Google Maps page</li>
     * </ul>
     * @param pageName String the page to open
     * @throws InterruptedException if the thread gets interrupted
     */
    @Then("^I verify a new (?:tab|window) opens(?: to)? the (.*?)$")
    public static void openNewWindow(String pageName) throws InterruptedException {
        PageManager.setPage(pageName);
        Driver.goToMostRecentWindow();
        PageManager.waitForPageLoad();
    }
    
    /**
     * Redirects to the given pageName
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I am redirected to the home page</li>
     * <li>I remain on the login page</li>
     * <li>I switch to the results window</li>
     * </ul>
     * 
     * @param pageName String the page to open
     * @throws InterruptedException if the thread gets interrupted
     */
    public static void switchTo(String pageName) throws InterruptedException {
    	switchTo(pageName, null);
    }
    
    /**
     * Opens the given pageName in an existing window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify a new tab opens the Home page</li>
     * <li>I verify a new window opens to the Login page</li>
     * <li>I verify a new tab opens to the Google Maps page</li>
     * </ul>
     * @param pageName String the page to open
     * @param direction String (next|previous) the window to open
     * @throws InterruptedException if the thread gets interrupted
     */
    @Then("I (?:am redirected to|remain on|switch to) the (.*?)(?: o?i?n the (previous|next) (?:window|tab))?$")
    public static void switchTo(String pageName, String direction) throws InterruptedException {
    	PageManager.setPage(pageName);
    	if (!StringUtils.isEmpty(direction)) {
        	if (direction.equals("next"))
        		Driver.goToNextWindow();
        	else
        		Driver.goToPreviousWindow();	
    	}
    	PageManager.waitForPageLoad();
    }

    /**
     * Verifies a file was downloaded successfully
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the results.csv file was successfully downloaded</li>
     * <li>I verify the lorem.zip file was successfully downloaded</li>
     * </ul>
     * @param fileName String the name of the vile to verify
     */
    @Then("^I verify the \"([^\"]*)\" file was successfully downloaded")
    public static void verifyFileDownloaded(String fileName) {
        String expectedResult = SentinelStringUtils.format("Expecting {} to be downloaded.", fileName);
        assertTrue(expectedResult, DownloadManager.isFileDownloaded(fileName));
    }
}

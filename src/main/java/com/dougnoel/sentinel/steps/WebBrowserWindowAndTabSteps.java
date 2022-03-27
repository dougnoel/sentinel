package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.framework.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WebBrowserWindowAndTabSteps {

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
     */
    @Then("^I verify a new (?:tab|window) opens(?: to)? the (.*)$")
    public static void openNewWindow(String pageName) {
        PageManager.setPage(pageName);
        Driver.goToNextWindow();
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
     */
    @Then("I switch to the (.*) o?i?n the (previous|next) (?:window|tab)$")
    public static void switchExistingWindow(String pageName, String direction) {
    	PageManager.setPage(pageName);
    	if (direction.equals("next"))
    		Driver.goToNextWindow();
    	else
    		Driver.goToPreviousWindow();
    }
}

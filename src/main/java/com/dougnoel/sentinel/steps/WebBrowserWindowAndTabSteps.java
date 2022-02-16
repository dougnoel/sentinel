package com.dougnoel.sentinel.steps;

import org.openqa.selenium.NoSuchWindowException;

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
        Driver.close();
    }
    
    /**
     * Opens the given pageName in a new window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I see a new tab open with the Home page</li>
     * <li>I see a new tab open with the Login page</li>
     * <li>I see a new tab open with the Google Maps page</li>
     * </ul>
     * @param pageName String the page to open
     * @throws NoSuchWindowException if the page doesn't load
     */
    @Then("^I verify a new (?:tab|window) opens(?: to)? the (.*)$")
    public static void openNewWindow(String pageName) throws NoSuchWindowException {
        PageManager.switchToNewWindow(pageName);
    }
    
    @Then("I (?:return|switch) focus (?:to|back to) the (.*)$")
    public static void switchExistingWindow(String pageName) throws NoSuchWindowException {
        PageManager.switchToExistingWindow(pageName);
    }
}

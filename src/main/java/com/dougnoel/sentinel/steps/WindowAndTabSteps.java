package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.pages.PageManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WindowAndTabSteps {

    /**
     * Closes the child browser tab or window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I close the browser tab</li>
     * <li>I close the browser window"</li>
     * </ul>
     * @throws InterruptedException if the page doesn't load
     */
    @When("^I close the browser (?:tab|window)$")
    public static void closeBrowserWindow() throws InterruptedException {
        PageManager.closeChildWindow();
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
     * @throws InterruptedException if the page doesn't load
     */
    @Then("^I verify a new (?:tab|window) opens(?: to)? the (.*)$")
    public static void openNewWindow(String pageName) throws InterruptedException {
        PageManager.switchToNewWindow(pageName);
    }

}

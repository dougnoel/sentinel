package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import org.apache.commons.lang3.StringUtils;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
        Driver.goToNewWindow();
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
    	switchTo(pageName, null, null);
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
     * @param title String title of the page to go to
     * @param direction String (next|previous) window to open
     * @throws InterruptedException if the thread gets interrupted
     */
    @Then("^I (?:am redirected to|remain on|switch to) the (.*?)(?:with the title \"([^\"]*)\")?(?: o?i?n the (previous|next) (?:window|tab))?$")
    public static void switchTo(String pageName, String title, String direction) throws InterruptedException {
    	PageManager.setPage(pageName);
    	if (!StringUtils.isEmpty(direction)) {
            if (direction.equals("next"))
                Driver.goToNextWindow();
            else if (direction.equals("previous"))
                Driver.goToPreviousWindow();
        }
        else if (!StringUtils.isEmpty(title))
            Driver.goToTitledWindow(title);

    	PageManager.waitForPageLoad();
    }

    /**
     * Opens the given pageName in an existing window that has a title containing the previously-used text.
     * <p></p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I look for and switch to a window on the Presidents Page with a title that contains the same text used for the preferences input</li>
     * <li>I look for and switch to a window on the Presidents Page with a title that contains the same text entered in the preferences input</li>
     * </ul>
     * @param pageName String name of the page to switch to.
     * @param key String the name of the element where the text was previously used, or the configuration variable used to lookup the stored value.
     * @throws InterruptedException if the thread gets interrupted.
     */
    @Then("^I look for and switch to a window on the (.*) with a title that contains the same text (?:entered|used) (?:for|in) the (.*?)$")
    public static void switchToContains(String pageName, String key) throws InterruptedException {
        PageManager.setPage(pageName);
        String configurationValue = Configuration.toString(key);
        if (!StringUtils.isEmpty(configurationValue))
            Driver.goToTitledWindowThatContains(configurationValue);

        PageManager.waitForPageLoad();
    }
}
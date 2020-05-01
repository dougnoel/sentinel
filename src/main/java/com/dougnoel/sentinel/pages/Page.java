package com.dougnoel.sentinel.pages;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Page class to contain a URL and the elements on the page.
 * <p>
 * <b>TO DO:</b>
 * <ul>
 * <li>Turn this into an abstract class.</li>
 * <li>Create a PageFactory</li>
 * <li>Abstract out the driver creation to allow multiple drivers to be created
 * at once</li>
 * </ul>
 */
public class Page {
    static protected final SelectorType CSS = SelectorType.CSS;
    static protected final SelectorType ID = SelectorType.ID;
    static protected final SelectorType NAME = SelectorType.NAME;
    static protected final SelectorType PARTIALTEXT = SelectorType.PARTIALTEXT;
    static protected final SelectorType TEXT = SelectorType.TEXT;
    static protected final SelectorType XPATH = SelectorType.XPATH;    

    protected WebDriver driver;

    protected URL url = null;

    /**
     * Initializes a WebDriver object for operating on page elements, and sets the
     * base URL for the page.
     */
    public Page() {
        driver = WebDriverFactory.getWebDriver();
        //TODO: Move this to a setup file somewhere else
        this.setImplicitWait(ConfigurationManager.getDefaultTimeout()); // Set a 10 second wait before erroring out on not finding elements.
    }

    /**
     * Sets an implicit wait time for the page before giving up on finding an
     * element.
     * <p>
     * <b>Caution:</b> This does not work well in conjunction with the selenium
     * explicit wait functionality.
     * 
     * TODO: Move this higher up and out of individual page objects.
     * 
     * @param timeInSeconds
     *            long
     * @return Page - Returns a page object for chaining.
     */
    public Page setImplicitWait(long timeInSeconds) {
        driver.manage().timeouts().implicitlyWait(timeInSeconds, TimeUnit.SECONDS);
        return this;
    }

    /**
     * Maximizes the browser window. Stores the current window size and position so
     * you can return to the existing settings.
     * 
     * TODO: Move this higher up and out of individual page objects.
     * 
     * @return Page - Returns a page object for chaining.
     */
    public Page maximizeWindow() {
        driver.manage().window().maximize();
        return this;
    }

    /**
     * Returns the title of the current web page we are on. Useful for debugging and
     * assertions.
     * 
     * @return String
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns the URL of the current web page we are on. Useful for debugging and
     * assertions.
     * 
     * @return String
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
}

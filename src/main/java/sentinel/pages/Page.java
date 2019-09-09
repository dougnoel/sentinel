package sentinel.pages;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import sentinel.utils.SelectorType;
import sentinel.utils.WebDriverFactory;

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

    private Dimension originalBrowserDimensions = null;
    private Point originalBrowserLocation = null;
    private Dimension lastBrowserDimensions = null;
    private Point lastBrowserLocation = null;

    /**
     * Initializes a WebDriver object for operating on page elements, and sets the
     * base URL for the page.
     */
    public Page() {
        driver = WebDriverFactory.getWebDriverAndHandleErrors();
        lastBrowserDimensions = originalBrowserDimensions = driver.manage().window().getSize();
        lastBrowserLocation = originalBrowserLocation = driver.manage().window().getPosition();
        this.setImplicitWait(10); // Set a 10 second wait before erroring out on not finding elements.
    }

    /**
     * Sets an implicit wait time for the page before giving up on finding an
     * element.
     * <p>
     * <b>Caution:</b> This does not work well in conjunction with the selenium
     * explicit wait functionality.
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
     * @return Page - Returns a page object for chaining.
     */
    public Page maximizeWindow() {
        lastBrowserDimensions = driver.manage().window().getSize();
        lastBrowserLocation = driver.manage().window().getPosition();
        driver.manage().window().maximize();
        return this;
    }

    /**
     * Undoes the previous window sizing and position operation.
     * 
     * @return Page - Returns a page object for chaining.
     */
    public Page undoWindowSizing() {
        // Store the existing dimensions so we can undo this change if we want to.
        Dimension currentBrowserDimensions = driver.manage().window().getSize();
        Point currentBrowserLocation = driver.manage().window().getPosition();
        // Go back to the last stored window size and location.
        driver.manage().window().setSize(lastBrowserDimensions);
        driver.manage().window().setPosition(lastBrowserLocation);
        // Store where we were so we can undo again later.
        lastBrowserDimensions = currentBrowserDimensions;
        lastBrowserLocation = currentBrowserLocation;
        return this;
    }

    /**
     * Restores the browser window size and location to the values set when the page
     * was first defined.
     * 
     * @return Page - Returns a page object for chaining.
     */
    public Page restoreOriginalWindowSize() {
        lastBrowserDimensions = driver.manage().window().getSize();
        lastBrowserLocation = driver.manage().window().getPosition();
        driver.manage().window().setSize(originalBrowserDimensions);
        driver.manage().window().setPosition(originalBrowserLocation);
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

/*
 * A suite of tests can have multiple browsers and operating systems
 * 
 * A test manipulates a website via a user The user moves through pages, and
 * utilizes elements
 * 
 * A Page Object contains elements It identifies and manipulates those elements
 * It has a URL part It has environments It needs to know where to get a base
 * URL from based on env
 * 
 * A website/portal contains multiple pages It has environments Those
 * environments have base URLs A website has users It will have users per
 * environment It should be able to create new users Users may or may not have
 * passwords
 */

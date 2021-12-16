package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.pages.PageManager;

import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Methods used to define basic operations for WinAppDriver.
 */
public class WinAppSteps {
    private static final Logger log = LogManager.getLogger(WinAppSteps.class.getName()); // Create a logger.
    
    @Before
    public static void before(Scenario scenario) {
        log.trace("Scenario ID: {} Scenario Name: {}", scenario.getId(), scenario.getName());
    }
    
    /**
     * Starts an application for testing, using the environment set to determine which version
     * of the app to load. The executable path is set in the page object yaml file.
     * Refer to the documentation in the sentinel.example project for more information.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I navigate to the Login Page</li>
     * <li>I am on the Main Page</li>
     * <li>I remain on the PopUp Page</li>
     * </ul>
     * @param pageName String Page Object Name
     */
    @Given("^I do nothing$")
    public static void NaviagateToWinApp(String pageName) {
    	BaseSteps.navigateToPageWithArguments("", pageName);
    }
    
	private static WindowsDriver windowsAppSession = null;
	
	@Given("that I open {string}")
	public void ProgramOpens(String pageName) {
    	PageManager.setPage(pageName);
    	String appToOpen = Configuration.executable();
    	log.debug("Loading the the {} page using the executable: {}", pageName, appToOpen);
        
	    // Write code here that turns the phrase above into concrete actions
		try {
			 DesiredCapabilities capabilities = new DesiredCapabilities();
			 capabilities.setCapability("app", appToOpen);
			 capabilities.setCapability("platformName","Windows");
			 capabilities.setCapability("deviceName", "WindowsPC");
			 windowsAppSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
			 windowsAppSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			} catch (Exception e) {
				if(windowsAppSession != null) {
					windowsAppSession.quit();
				}
			 
			 throw new io.cucumber.core.exception.CucumberException(appToOpen + " failed to start", e);
		  }
	}
	
	@When("I enter {string} into the {string} field")
	public void IEnterText(String inputText, String editLocator) {
		windowsAppSession.findElementByName(editLocator).sendKeys(inputText);
	}
	
	@When("I press the {string} button")
	public void IPressTheButtons(String buttonToPress) {
		windowsAppSession.findElementByName(buttonToPress).click();
	}
	
	@Then("I expect that {string} contains {string}")
	public void i_expect_the_result_pane_to_contain_the_output(String resultLocator, String expectedResult) {
		String actualResult = windowsAppSession.findElementByName(resultLocator).getText().trim();
		assertEquals(expectedResult, actualResult);
		
		try { windowsAppSession.findElementByName(resultLocator).clear(); } catch(Exception e) { /*This is a do not save catch. Errors here can be ignored*/ }
		windowsAppSession.closeApp(); //close app should have a handler if a "Close without saving" window spawns to avoid the above
	}
    
}
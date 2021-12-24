package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;
import com.dougnoel.sentinel.webdrivers.WinAppDriverFactory;

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
    
	private static WindowsDriver windowsAppSession = null; //TODO: Tyler - This was in the middle of the code. It should not be there.
	
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
	
	@SuppressWarnings("unchecked")
	@Given("that I open {string}")
	public void ProgramOpens(String pageName) {
    	PageManager.setPage(pageName); //Set page name to windows app
        
    	//Create the Driver
    	windowsAppSession = (WindowsDriver<WebElement>) Driver.getDriver();

	}
	
	@When("I enter {string} into the {string} field")
	public void IEnterText(String inputText, String editLocator) {
		windowsAppSession.findElementByName(editLocator).sendKeys(inputText);
	}
	
	@When("I press the {string} button")
	public void IPressTheButtons(String buttonToPress) {
//		BaseSteps.click(buttonToPress);
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
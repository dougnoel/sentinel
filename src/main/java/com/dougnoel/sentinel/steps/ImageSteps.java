package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;

public class ImageSteps {
	private static Scenario scenario;
	
	@Before
    public static void before(Scenario scenario) {
		ImageSteps.scenario = scenario;
	}
	
	/**
	 * Takes a screenshot of the given element and stores it for later comparison
	 * @param elementName String name of the element to screenshot
	 * @throws IOException
	 */
	@Test
    @When("^I take a screenshot of (?:the|a) (page|.*)$")
    public static void storeScreenshotOfElement(String elementName) throws IOException {
    	String imageFileName = scenario.getName()+ "_" + elementName + "_" + PageManager.getPage().getName() + ".png";
    	File screenshotFile;
    	
    	//Determine if we're taking a screenshot of an element or the whole page.
    	if(!elementName.toLowerCase().contentEquals("page")) {
    		screenshotFile = getElement(elementName).getScreenshot();
    	}
    	else {
        	TakesScreenshot pageScreenshotTool =((TakesScreenshot) WebDriverFactory.getWebDriver());
        	screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);
    	}
    	File destinationFile = new File("logs/expected/" + imageFileName);
    	FileUtils.copyFile(screenshotFile, destinationFile);
    }
}
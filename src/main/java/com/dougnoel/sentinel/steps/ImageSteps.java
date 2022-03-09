package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.cucumber.java.en.When;

public class ImageSteps {
	private static WebDriver driver() { return WebDriverFactory.getWebDriver(); }
	
	/**
	 * Takes a screenshot of the given element and stores it for later comparison
	 * @param elementName String name of the element to screenshot
	 * @throws IOException
	 */
    @When("^I take a screenshot of (?:the|a) (page|.*)$")
    public static void storeScreenshotOfElement(String elementName) throws IOException {
    	TakesScreenshot pageScreenshotTool =((TakesScreenshot)driver());
    	File screenshotFile;
    	
    	if(!elementName.contentEquals("page")) {
    		screenshotFile = getElement(elementName).getScreenshot();
    	}
    	else {
    		screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);
    	}
    	
    	File destinationFile = new File("logs/expected/" + elementName + ".png");
    	FileUtils.copyFile(screenshotFile, destinationFile);
    }
}
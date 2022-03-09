package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import io.cucumber.java.en.When;

public class ImageSteps {

	/**
	 * Takes a screenshot of the given element and stores it for later comparison
	 * @param elementName String name of the element to screenshot
	 * @throws IOException
	 */
    @When("^I take a screenshot of (?:the|a) (page|.*)$")
    public static void storeScreenshotOfElement(String elementName) throws IOException {
    	File screenshotFile = getElement(elementName).getScreenshot();
    	File destinationFile = new File("logs/expected/" + elementName + ".png");
    	FileUtils.copyFile(screenshotFile, destinationFile);
    }
}
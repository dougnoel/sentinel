package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import java.io.File;
import java.io.IOException;

import com.dougnoel.sentinel.webdrivers.Driver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.dougnoel.sentinel.system.FileManager;
import com.dougnoel.sentinel.pages.PageManager;

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
	 * @throws IOException If the expected image to be created fails to save
	 */
    @When("^I take a screenshot of (?:the|a) (.*?)$")
    public static void storeScreenshotOfElement(String elementName) throws IOException {
		String outputFolder = "imageComparison/" + scenario.getName();
    	String imageFileName = PageManager.getPage().getName() + "_" + elementName + "_EXPECTED" + ".png";
    	File screenshotFile;

    	//Determine if we're taking a screenshot of an element or the whole page.
    	if(!elementName.toLowerCase().contentEquals("page") && !elementName.toLowerCase().contentEquals("window")) {
    		screenshotFile = getElement(elementName).getScreenshot();
    	}
    	else {
        	TakesScreenshot pageScreenshotTool =((TakesScreenshot) Driver.getWebDriver());
        	screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);
    	}
    	
    	FileManager.saveImage(outputFolder, imageFileName, screenshotFile);
    }
}
package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.dougnoel.sentinel.system.FileManager;
import com.dougnoel.sentinel.pages.PageManager;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;

public class ImageSteps {
	private static Scenario scenario;
	protected static final Logger log = LogManager.getLogger(Element.class.getName()); // Create a logger.

	@Before
    public static void before(Scenario scenario) {
		ImageSteps.scenario = scenario;
	}
	
	/**
	 * Takes a screenshot of the given element and stores it for later comparison
	 * @param elementName String name of the element to screenshot
	 * @throws IOException If the expected image to be created fails to save
	 */
    @When("^I take a screenshot of (?:the|a) (page|window|.*?)$")
    public static void storeScreenshotOfElement(String elementName) throws IOException {
    	String imageFileName = "tempTocompare" + ".png";
    	File screenshotFile;
		PageObjectType pageType = PageManager.getCurrentPageObjectType();

		if(elementName.matches("(?i)^\\s*(page|window)\\s*$")){
			TakesScreenshot pageScreenshotTool =((TakesScreenshot) Driver.getWebDriver());
			screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);

			switch(pageType){
				case EXECUTABLE:
					BufferedImage cropWindow = FileManager.readImage(screenshotFile);
					BufferedImage croppedImage = cropWindow.getSubimage(2,2,(cropWindow.getWidth()-4),(cropWindow.getHeight()-4));
					FileManager.saveImage(null, imageFileName, croppedImage);
					return;
				case WEBPAGE:
					break;
				case UNKNOWN:
				default:
					log.warn("Page object type is of the unhandled {} type. Page/Window screenshot may not be accurate or supported.", pageType);
					break;
			}
		} else{
			screenshotFile = getElement(elementName).getScreenshot();
		}

		FileManager.saveImage(null, imageFileName, screenshotFile);
    }
}
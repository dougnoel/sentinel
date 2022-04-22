package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.dougnoel.sentinel.elements.WindowsElement;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.system.FileManager;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;

public class ImageVerificationSteps {
	private static Scenario scenario;
	
	@Before
	public static void before(Scenario scenario) {
		ImageVerificationSteps.scenario = scenario;
	}
	
	/**
	 * Takes a screenshot of the given element and compares it to the previously-stored image of that same element.
	 * Default pixel tolerance level = 0.1. Defaults pixel location threshold = 5.
	 * @param elementName String the name of the element to capture and compare.
	 * @param assertion String the user input determining if we expect a match or a mismatch.
	 * 
	 * @throws IOException if file creation does not work.
	 */
	@Then("^I verify (?:the|an?) (.*?) (do(?:es)? not )?match(?:es)? the (?:expected|original) image$")
    public static void verifyImageNotMatch(String elementName, String assertion) throws IOException {
        boolean negate = !StringUtils.isEmpty(assertion);
        var expectedResult = SentinelStringUtils.format("Expected {} to {}match its previous state visually.",
                elementName, (negate ? "not " : ""));
        
		ImageComparisonResult comparisonResult = compareImages(elementName);
        
        //Check the result after determining if we're doing a should match or should not match.
		if(negate) {
			assertNotEquals(comparisonResult.getImageComparisonState(), ImageComparisonState.MATCH);
		}
		else {
			assertEquals(comparisonResult.getImageComparisonState(), ImageComparisonState.MATCH);
		}
	}
	
	/**
	 * Takes an updated screenshot of the current element, or page, for comparison to an earlier expected image.
	 * <br><br>
	 * <i>
	 *     If screenshots are different sizes, both will be enlarged to the largest dimensions found. Expanded space
	 * will be set to parent background color for web, upper-left-most color for windows applications.
	 * </i>
	 *
	 * @param elementName String the name of the element to capture and compare 
	 * or any casing of "page" alone to compare the entire page.
	 * @throws IOException if file creation does not work
	 * 
	 * @return ImageComparisonResult the image comparison result.
	 */
    private static ImageComparisonResult compareImages(String elementName) throws IOException {
		String outputFolder = "ImageComparison/" + scenario.getName();
    	String actualFileName = PageManager.getPage().getName() + "_" + elementName + "_ACTUAL" + ".png";
		String expectedFileName = PageManager.getPage().getName() + "_" + elementName + "_EXPECTED" + ".png";
    	String resultImageFileName = PageManager.getPage().getName() + "_" + elementName + "_RESULT" + ".png";
    	File screenshotFile;
    	Color backgroundColor;
    	
    	//Determine if we're taking a screenshot of the page or element.
    	if(!elementName.toLowerCase().contentEquals("page") && !elementName.toLowerCase().contentEquals("window")) {
    		screenshotFile = getElement(elementName).getScreenshot();
			if(PageManager.getCurrentPageObjectType() != PageObjectType.EXECUTABLE){
				backgroundColor = getElement(elementName).getBackgroundColor();
			}
			else{
				backgroundColor = ((WindowsElement) getElement(elementName)).getColor().getColor();
			}
    	}
    	else {
    		TakesScreenshot pageScreenshotTool =((TakesScreenshot) Driver.getWebDriver());
    		screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);

			if(PageManager.getCurrentPageObjectType() != PageObjectType.EXECUTABLE) {
				Element body = new Element("body", Map.of("xpath", "//body"));
				backgroundColor = body.getBackgroundColor();
			}
			else{
				WindowsElement appWindow = new WindowsElement("window", Map.of("xpath", "/*"));
				backgroundColor = appWindow.getColor().getColor();
			}
    	}
    	
    	FileManager.saveImage(outputFolder, actualFileName, screenshotFile);

    	//load images to be compared:
        BufferedImage expectedImage = FileManager.readImage(outputFolder, expectedFileName);
        BufferedImage actualImage = FileManager.readImage(outputFolder, actualFileName);
        
        //If the image sizes are different then enlarge them to the largest size width and height of both
		int expectedHeight = expectedImage.getHeight();
		int expectedWidth = expectedImage.getWidth();
		int actualHeight = actualImage.getHeight();
		int actualWidth = actualImage.getWidth();

		if((expectedWidth != actualWidth) || (expectedHeight != actualHeight)) {
			int newHeight;
			int newWidth;
		
			//Calculate the largest width and height to set both images to
			newWidth = Math.max(expectedWidth, actualWidth);
			newHeight = Math.max(expectedHeight, actualHeight);
			
			BufferedImage newExpected = new BufferedImage(newWidth, newHeight, expectedImage.getType());
			BufferedImage newActual = new BufferedImage(newWidth, newHeight, expectedImage.getType());
			
			//Resize the images, using the background color to draw new space
			Graphics2D g2 = newExpected.createGraphics();
			g2.setPaint(backgroundColor);
			g2.fillRect(0, 0, newWidth, newHeight);
			g2.setColor(backgroundColor);
			g2.drawImage(expectedImage, null, 0, 0);
			expectedImage = newExpected;
			
			g2 = newActual.createGraphics();
			g2.setPaint(backgroundColor);
			g2.fillRect(0, 0, newWidth, newHeight);
			g2.setColor(backgroundColor);
			g2.drawImage(actualImage, null, 0, 0);
			actualImage = newActual;
			g2.dispose();
		}
        
		//Compare the two images, writing the result to disk
		ImageComparison comparison = new ImageComparison(expectedImage, actualImage);
		ImageComparisonResult comparisonResult = comparison.compareImages();
		FileManager.saveImage(outputFolder, resultImageFileName, comparisonResult.getResult());
        
        return comparisonResult;
    }
}

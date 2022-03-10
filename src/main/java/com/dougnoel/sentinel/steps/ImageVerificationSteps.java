package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;

public class ImageVerificationSteps {
	private static Scenario scenario;
	
	@Before
	public void before(Scenario scenario) {
		ImageVerificationSteps.scenario = scenario;
	}
	
	/**
	 * Takes a screenshot of the given element and compares it to the previously-stored image of that same element.
	 * Default pixel tolerance level = 0.1. Defaults pixel location threshold = 5.
	 * @param elementName String the name of the element to capture and compare.
	 * @throws IOException if file creation does not work
	 */
	@Then("^I verify (?:the|an?) (.*?) (do(?:es)? not )?match(?:es)? the (?:expected|original) image$")
    public static void verifyImageNotMatch(String elementName, String assertion) throws IOException {
        boolean negate = !StringUtils.isEmpty(assertion);
        var expectedResult = SentinelStringUtils.format("Expected {} to {}match its previous state visually.",
                elementName, (negate ? "not " : ""));
        
		ImageComparisonResult comparisonResult = compareImages(elementName);
		
		String imageFileName = scenario.getName()+ "_" + elementName + "_" + PageManager.getPage().getName() + ".png";
		File resultDestination = new File( "logs/diff/" + imageFileName );
		
		//Save the image comparison we obtained.
		comparisonResult.writeResultTo(resultDestination);
        
        //Check the result after determining if we're doing a should match or should not match.
		if(negate) {
			assertNotEquals(expectedResult, ImageComparisonState.MATCH, comparisonResult.getImageComparisonState());
		}
		else {
			assertEquals(expectedResult, ImageComparisonState.MATCH, comparisonResult.getImageComparisonState());
		}
	}
	
	/**
	 * Takes an updated screenshot of the current element or page for comparison.
	 * Resizes the image canvases to be the same if different, and then runs a comparison of the two images.
	 * @param elementName String the name of the element to capture and compare 
	 * or any casing of "page" alone to compare the entire page.
	 * @throws IOException if file creation does not work
	 * 
	 * @return ImageComparisonResult the image comparison result.
	 */
    private static ImageComparisonResult compareImages(String elementName) throws IOException {
    	String imageFileName = scenario.getName()+ "_" + elementName + "_" + PageManager.getPage().getName() + ".png";
    	File screenshotFile;
    	Color backgroundColor;
    	
    	//Determine if we're taking a screenshot of the page or element.
    	if(!elementName.toLowerCase().contentEquals("page")) {
    		screenshotFile = getElement(elementName).getScreenshot();
    		backgroundColor = getElement(elementName).getBackgroundColor();
    	}
    	else {
    		TakesScreenshot pageScreenshotTool =((TakesScreenshot) WebDriverFactory.getWebDriver());
    		screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);
    		backgroundColor = PageManager.getPageBackgroundColor();
    	}
    	
    	File destinationFile = new File("logs/actual/" + imageFileName);
    	FileUtils.copyFile(screenshotFile, destinationFile);

    	//load images to be compared:
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources("logs/expected/" + imageFileName);
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources("logs/actual/" + imageFileName);
        
        int expectedHeight = expectedImage.getHeight();
    	int expectedWidth = expectedImage.getWidth();
    	int actualHeight = actualImage.getHeight();
    	int actualWidth = actualImage.getWidth();
    	
        //If the image sizes are different, make them equivalent.
        //TODO: Needs to have the background color handled better.
        if((expectedWidth != actualWidth) || (expectedHeight != actualHeight)) {
        	int newHeight;
        	int newWidth;

        	//Calculate the new width and height to set the images to.
        	if(expectedWidth > actualWidth) {
        		newWidth = expectedWidth;
        	}
        	else {
        		newWidth = actualWidth;
        	}
        	
        	if(expectedHeight > actualHeight) {
        		newHeight = expectedHeight;
        	}
        	else {
        		newHeight = actualHeight;
        	}
        	
        	BufferedImage newExpected = new BufferedImage(newWidth, newHeight, expectedImage.getType());
        	BufferedImage newActual = new BufferedImage(newWidth, newHeight, expectedImage.getType());
        	
        	//TODO: Verify this works with different colored backgrounds than white.
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
        
        //Create ImageComparison object and compare the images.
        ImageComparison comparison = new ImageComparison(expectedImage, actualImage);
        return comparison.compareImages();
    }
}

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
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.webdrivers.WebDriverFactory;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import io.cucumber.java.en.Then;

public class ImageVerificationSteps {
	private static WebDriver driver() { return WebDriverFactory.getWebDriver(); }
	
	/**
	 * Takes a screenshot of the given element and compares it to the previously-stored image of that same element.
	 * Default pixel tolerance level = 0.1. Defaults pixel location threshold = 5.
	 * @param elementName String the name of the element to capture and compare.
	 * @throws IOException
	 */
	@Then("^I verify (?:the|a) (.*) (do not match|does not match|matches) the (?:expected|original) image$")
    public static void verifyImageNotMatch(String elementName, String matchCondition) throws IOException {
		//TODO: This should have the time removed and a name (scenario name?) added so we don't flood the system with screenshots
		ImageComparisonResult comparisonResult = compareImages(elementName);
		File resultDestination = new File( "logs/diff/" + System.currentTimeMillis() + "_" + elementName + "_" + comparisonResult.getImageComparisonState() +".png" );
		
		//Image can be saved after comparison, using ImageComparisonUtil.
		comparisonResult.writeResultTo(resultDestination);
        
        //Check the result
		if(matchCondition.contains("not")) {
			assertNotEquals(ImageComparisonState.MATCH, comparisonResult.getImageComparisonState());
		}
		else {
			assertEquals(ImageComparisonState.MATCH, comparisonResult.getImageComparisonState());
		}
	}
	
    private static ImageComparisonResult compareImages(String elementName) throws IOException {
    	TakesScreenshot pageScreenshotTool =((TakesScreenshot)driver());
    	File screenshotFile;
    	Color backgroundColor;
    	
    	if(!elementName.contentEquals("page")) {
    		screenshotFile = getElement(elementName).getScreenshot();
    		backgroundColor = getElement(elementName).getBackgroundColor();
    	}
    	else {
    		screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);
    		backgroundColor = getPageBackground();
    	}
    	
    	File destinationFile = new File("logs/actual/" + elementName + ".png");
    	FileUtils.copyFile(screenshotFile, destinationFile);

    	//load images to be compared:
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources("logs/expected/" + elementName + ".png");
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources("logs/actual/" + elementName + ".png");
        
        //If the image sizes are different, make them equivalent
        //TODO: Needs to have the background color handled better
        if((expectedImage.getWidth() != actualImage.getWidth()) || (expectedImage.getHeight() != actualImage.getHeight())) {
        	int newHeight;
        	int newWidth;
        	int expectedHeight = expectedImage.getHeight();
        	int expectedWidth = expectedImage.getWidth();
        	int actualHeight = actualImage.getHeight();
        	int actualWidth = actualImage.getWidth();

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
        	
        	//Replace backgroundColor with Color.black to see the resize canvas effect
        	Graphics2D g2 = newExpected.createGraphics();
        	g2.setPaint(Color.white);
        	g2.fillRect(0, 0, newWidth, newHeight);
        	g2.setColor(backgroundColor);
        	g2.drawImage(expectedImage, null, 0, 0);
        	expectedImage = newExpected;
        	
        	g2 = newActual.createGraphics();
        	g2.setPaint(Color.white);
        	g2.fillRect(0, 0, newWidth, newHeight);
        	g2.setColor(backgroundColor);
        	g2.drawImage(actualImage, null, 0, 0);
        	actualImage = newActual;
        	g2.dispose();
        }

        //Create ImageComparison object and compare the images.
        //Defaults to 0.1 pixel tolerance level and 5 pixel location threshold
//ImageComparisonResult imageComparisonResult = new ImageComparison(expectedImage, actualImage).compareImages();
        
        //Create ImageComparison object and compare the images.
        var comparison = new ImageComparison(expectedImage, actualImage);
        ImageComparisonResult imageComparisonResult = comparison.compareImages();
        
        return imageComparisonResult;
    }
    
    private static Color getPageBackground()
	{  
    	try {
    		WebElement element = driver().findElement(By.tagName("body"));
    		var color = element.getCssValue("background-color");
    		Color bgColor = org.openqa.selenium.support.Color.fromString(color).getColor();
    		return bgColor;
    	}
		catch(NoSuchElementException e) {
			return Color.white;
		}
	}
}

package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertEquals;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import io.cucumber.java.en.Then;

public class ImageVerificationSteps {

	
	/**
	 * Takes a screenshot of the given element and compares it to the previously-stored image of that same element.
	 * Default pixel tolerance level = 0.1. Defaults pixel location threshold = 5.
	 * @param elementName String the name of the element to capture and compare.
	 * @throws IOException
	 */
    @Then("^I verify (?:the|a) (.*) matches the expected image$")
    public static void verifyImageMatch(String elementName) throws IOException {
    	File screenshotFile = getElement(elementName).getScreenshot();
    	File destinationFile = new File("logs/actual/" + elementName + ".png");
    	FileUtils.copyFile(screenshotFile, destinationFile);

    	//load images to be compared:
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources("logs/expected/" + elementName + ".png");
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources("logs/actual/" + elementName + ".png");
        File resultDestination = new File( "logs/diff/" + elementName + ".png" );

        //Create ImageComparison object and compare the images.
        //Defaults to 0.1 pixel tolerance level and 5 pixel location threshold
        ImageComparisonResult imageComparisonResult = new ImageComparison(expectedImage, actualImage).compareImages();
        
        //Image can be saved after comparison, using ImageComparisonUtil.
        ImageComparisonUtil.saveImage(resultDestination, imageComparisonResult.getResult()); 
        
        //Check the result
        assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
    }
}

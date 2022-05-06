package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	protected static final Logger log = LogManager.getLogger(Element.class.getName()); // Create a logger.
	
	@Before
	public static void before(Scenario scenario) {
		ImageVerificationSteps.scenario = scenario;
	}
	
	/**
	 * Takes a screenshot of the given element and compares it to the previously-stored image of that same element.
	 * Default scaling pixel tolerance level = 0.06%.
	 * This translates to ~1244 pixels. Roughly a 35x35 image area at 1080p resolution.
	 * @param elementName String the name of the element to capture and compare.
	 * @param assertion String the user input determining if we expect a match or a mismatch.
	 */
	@Then("^I verify (?:the|an?) (page|window|.*?) (do(?:es)? not )?match(?:es)? the (expected|original|previous|.*?) image$")
    public static void verifyImageComparison(String elementName, String assertion, String optionalStoredImage) {
        boolean negate = !StringUtils.isEmpty(assertion);
        var expectedResult = SentinelStringUtils.format("Expected {} to {}match its previous state visually.",
                elementName, (negate ? "not " : ""));
		String storedImage = null;

		if(!optionalStoredImage.matches("(?i)^\\s*(expected|original|previous)\\s*$")){
			storedImage = optionalStoredImage;
		}
        
		ImageComparisonResult comparisonResult = compareImages(elementName, storedImage);
        
        //Check the result after determining if we're doing a should match or should not match.
		if(negate) {
			assertNotEquals(expectedResult, comparisonResult.getImageComparisonState(), ImageComparisonState.MATCH);
		}
		else {
			assertEquals(expectedResult, comparisonResult.getImageComparisonState(), ImageComparisonState.MATCH);
		}
	}
	
	/**
	 * Takes an updated screenshot of the current element, or page, for comparison to an earlier expected image.
	 * <br><br>
	 * <i>
	 * If screenshots are different sizes, both will be enlarged to the largest dimensions found. Expanded space
	 * will be set to parent background color for web, upper-left-most color for windows applications.
	 * </i>
	 *
	 * @param elementName String the name of the element to capture and compare 
	 * or any casing of "page" alone to compare the entire page.
	 * 
	 * @return ImageComparisonResult the image comparison result.
	 */
    private static ImageComparisonResult compareImages(String elementName, String optionalStoredImage) {
		//Set file output/input strings and page type
		String outputFolder = "ImageComparison/" + scenario.getName();
		String actualFileName = PageManager.getPage().getName() + "_" + elementName + "_ACTUAL" + ".png";
		String expectedFileName = PageManager.getPage().getName() + "_" + elementName + "_EXPECTED" + ".png";
		String resultImageFileName = PageManager.getPage().getName() + "_" + elementName + "_RESULT" + ".png";
		PageObjectType pageType = PageManager.getCurrentPageObjectType();

		//Load stored image if we're not comparing to a previous step screenshot
		File testDataImageLocation = null;
		if(optionalStoredImage != null){
			testDataImageLocation = new File(Configuration.getTestdataValue("images", optionalStoredImage));
		}

    	//load images to be compared
		BufferedImage expectedImage;
		File actualScreenshot = processActualScreenshot(pageType, elementName);
		BufferedImage actualImage = FileManager.readImage(actualScreenshot);
		if(optionalStoredImage != null) {
			expectedImage = FileManager.readImage(testDataImageLocation);
		} else {
			expectedImage = FileManager.readImage(outputFolder, expectedFileName);
		}

        //Ensure image sizes are equalized to enforce writing of a result file regardless of sizes
		Map<String, BufferedImage> equalizedImages;
		equalizedImages = equalizeImages(pageType, elementName, expectedImage, actualImage);
        
		//Compare the two images
		ImageComparison comparison = new ImageComparison(equalizedImages.get("expected"), equalizedImages.get("actual"));
		comparison.setAllowingPercentOfDifferentPixels(0.06); //0.06% Will allow for a ~51 pixel difference. A text cursor is ~38
		ImageComparisonResult comparisonResult = comparison.compareImages();

		//Write results to disk
		FileManager.saveImage(outputFolder, actualFileName, actualScreenshot);
		FileManager.saveImage(outputFolder, expectedFileName, expectedImage);
		FileManager.saveImage(outputFolder, resultImageFileName, comparisonResult.getResult());
        
        return comparisonResult;
    }

	private static File processActualScreenshot(PageObjectType pageType, String elementName){
		File screenshotFile;

		if(elementName.matches("(?i)^\\s*(page|window)\\s*$")){
			//Process window/page screenshot
			TakesScreenshot pageScreenshotTool =((TakesScreenshot) Driver.getWebDriver());
			screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);

			switch(pageType){
				case EXECUTABLE:
					BufferedImage cropWindow = FileManager.readImage(screenshotFile);
					BufferedImage croppedImage = cropWindow.getSubimage(2,2,(cropWindow.getWidth()-4),(cropWindow.getHeight()-4));
					screenshotFile = FileManager.saveImage(null, "tempScreenshotWindow.png", croppedImage);
					break;
				case WEBPAGE:
				case UNKNOWN:
				default:
					break;
			}
		} else{
			//Process element screenshot
			screenshotFile = getElement(elementName).getScreenshot();
		}

		return screenshotFile;
	}

	private static Color processBackgroundColor(PageObjectType pageType, String elementName){
		//Return web page/element background or default red for other types
		if(elementName.matches("(?i)^\\s*(page|window)\\s*$")){
			switch(pageType){
				case WEBPAGE:
					Element body = new Element("body", Map.of("xpath", "//body"));
					return body.getBackgroundColor();
				case EXECUTABLE:
					log.warn("Page object type is of the {} type. Red will be used as background color fallback.", pageType);
				case UNKNOWN:
				default:
					log.warn("Page object type is of the unhandled {} type. Comparison may be inaccurate and red will be used as background color fallback.", pageType);
					break;
			}
		} else{
			switch(pageType){
				case WEBPAGE:
					return getElement(elementName).getBackgroundColor();
				case EXECUTABLE:
					log.warn("Page object type is of the {} type. Red will be used as background color fallback.", pageType);
					break;
				case UNKNOWN:
				default:
					log.warn("Page object type is of the unhandled {} type. Comparison may be inaccurate and red will be used as background color fallback.", pageType);
					break;
			}
		}

		return Color.red;
	}

	private static Map<String, BufferedImage> equalizeImages(PageObjectType pageType, String elementName, BufferedImage expectedImage, BufferedImage actualImage){
		int expectedHeight = expectedImage.getHeight();
		int expectedWidth = expectedImage.getWidth();
		int actualHeight = actualImage.getHeight();
		int actualWidth = actualImage.getWidth();
		Color backgroundColor = processBackgroundColor(pageType, elementName);
		Map<String, BufferedImage> equalizedImages = new HashMap<>();

		//If image sizes are not equal, equalize them
		//If they are equal, return as-is
		if((expectedWidth != actualWidth) || (expectedHeight != actualHeight)) {
			log.warn("Page object has been detected as {} with different sized comparison snapshots. This may result in an unreliable comparison.", pageType);

			int newHeight;
			int newWidth;

			//Calculate the height and width to draw the smaller image to
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

		equalizedImages.put("expected", expectedImage);
		equalizedImages.put("actual", actualImage);

		return equalizedImages;
	}
}
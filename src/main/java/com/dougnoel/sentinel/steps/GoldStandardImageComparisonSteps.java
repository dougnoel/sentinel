package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.elements.WindowsElement;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import com.dougnoel.sentinel.webdrivers.Driver;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.Then;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GoldStandardImageComparisonSteps {
    private static Scenario scenario;
    protected static final Logger log = LogManager.getLogger(GoldStandardImageComparisonSteps.class.getName()); // Create a logger.


    //TODO: Combine this with imageverification steps
    @Before
    public static void before(Scenario scenario) {
        GoldStandardImageComparisonSteps.scenario = scenario;
    }

    /**
     * Takes a screenshot of the given element and compare it to the testdata referenced image of that same element.
     * Default scaling pixel tolerance level = 0.06%.
     * This translates to ~1244 pixels. Roughly a 35x35 image area at 1080p resolution.
     * @param elementName String the name of the element to capture and compare.
     * @param assertion String the user input determining if we expect a match or a mismatch.
     */
    @Then("^I verify (?:the|an?) (page|window|.*?) (do(?:es)? not )?match(?:es)? the (.*?) gold standard image$")
    public static void verifyScreenshotAgainstTestDataImage(String elementName, String assertion, String testDataImageName) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected {} to {}match its gold standard state visually.",
                elementName, (negate ? "not " : ""));

        ImageComparisonResult comparisonResult = performPremadeExpectedImageComparison(elementName, testDataImageName);

        //Check the result after determining if we're doing a should match or should not match.
        if(negate) {
            assertNotEquals(expectedResult, comparisonResult.getImageComparisonState(), ImageComparisonState.MATCH);
        }
        else {
            assertEquals(expectedResult, comparisonResult.getImageComparisonState(), ImageComparisonState.MATCH);
        }
    }

    /**
     * Takes an updated screenshot of the current element, or page, for comparison to an earlier testdata referenced image.
     * <br><br>
     * <i>
     * If screenshots are different sizes, both will be enlarged to the largest dimensions found. Expanded space
     * will be set to parent background color for web, upper-left-most color for windows applications.
     * </i>
     *
     * @param elementName String the name of the element to capture and compare
     * or any casing of "page" alone to compare the entire page.
     * @return ImageComparisonResult the image comparison result.
     */
    private static ImageComparisonResult performPremadeExpectedImageComparison (String elementName, String testDataImageName) {
        String outputFolder = "ImageComparison/" + scenario.getName();
        String expectedFileName = PageManager.getPage().getName() + "_" + elementName + "_EXPECTED" + ".png";
        String actualFileName = PageManager.getPage().getName() + "_" + elementName + "_ACTUAL" + ".png";
        String resultImageFileName = PageManager.getPage().getName() + "_" + elementName + "_RESULT" + ".png";
        File testDataImageLocation = new File(Configuration.getTestdataValue("images", testDataImageName));

        File screenshotFile;
        Color backgroundColor;
        PageObjectType pageType = PageManager.getCurrentPageObjectType();

        if(elementName.matches("(?i)^\\s*(page|window)\\s*$")){
            TakesScreenshot pageScreenshotTool =((TakesScreenshot) Driver.getWebDriver());
            screenshotFile = pageScreenshotTool.getScreenshotAs(OutputType.FILE);

            switch(pageType){
                case EXECUTABLE:
                    BufferedImage cropWindow = FileManager.readImage(screenshotFile);
                    BufferedImage croppedImage = cropWindow.getSubimage(2,2,(cropWindow.getWidth()-4),(cropWindow.getHeight()-4));
                    screenshotFile = FileManager.saveImage(null, "tempScreenshotWindow.png", croppedImage);

                    WindowsElement appWindow = new WindowsElement("window", Map.of("xpath", "/*"));
                    backgroundColor = appWindow.getColorAtOffset(3,3).getColor();
                    break;
                case WEBPAGE:
                    Element body = new Element("body", Map.of("xpath", "//body"));
                    backgroundColor = body.getBackgroundColor();
                    break;
                case UNKNOWN:
                default:
                    log.warn("Page object type is of the unhandled {} type. Black will be used as background color fallback.", pageType);
                    backgroundColor = Color.BLACK;
                    break;
            }
        } else{
            screenshotFile = getElement(elementName).getScreenshot();

            switch(pageType){
                case EXECUTABLE:
                    backgroundColor = ((WindowsElement) getElement(elementName)).getColorAtOffset().getColor();
                    break;
                case WEBPAGE:
                    backgroundColor = getElement(elementName).getBackgroundColor();
                    break;
                case UNKNOWN:
                default:
                    log.warn("Page object type is of the unhandled {} type. Black will be used as background color fallback.", pageType);
                    backgroundColor = Color.BLACK;
                    break;
            }
        }

        FileManager.saveImage(outputFolder, actualFileName, screenshotFile);

        //load images to be compared:
        BufferedImage testDataImage = FileManager.readImage(testDataImageLocation);
        FileManager.saveImage(outputFolder, expectedFileName, testDataImage);
        BufferedImage actualImage = FileManager.readImage(outputFolder, actualFileName);

        //If the image sizes are different then enlarge them to the largest size width and height of both
        int expectedHeight = testDataImage.getHeight();
        int expectedWidth = testDataImage.getWidth();
        int actualHeight = actualImage.getHeight();
        int actualWidth = actualImage.getWidth();

        if((expectedWidth != actualWidth) || (expectedHeight != actualHeight)) {
            switch(pageType){
                case WEBPAGE:
                    break;
                case EXECUTABLE:
                case UNKNOWN:
                default:
                    log.warn("Page object has been detected as {} with different sized comparison snapshots. This may result in an unreliable comparison.", pageType);
                    break;
            }

            int newHeight;
            int newWidth;

            //Calculate the largest width and height to set both images to
            newWidth = Math.max(expectedWidth, actualWidth);
            newHeight = Math.max(expectedHeight, actualHeight);

            BufferedImage newExpected = new BufferedImage(newWidth, newHeight, testDataImage.getType());
            BufferedImage newActual = new BufferedImage(newWidth, newHeight, testDataImage.getType());

            //Resize the images, using the background color to draw new space
            Graphics2D g2 = newExpected.createGraphics();
            g2.setPaint(backgroundColor);
            g2.fillRect(0, 0, newWidth, newHeight);
            g2.setColor(backgroundColor);
            g2.drawImage(testDataImage, null, 0, 0);
            testDataImage = newExpected;

            g2 = newActual.createGraphics();
            g2.setPaint(backgroundColor);
            g2.fillRect(0, 0, newWidth, newHeight);
            g2.setColor(backgroundColor);
            g2.drawImage(actualImage, null, 0, 0);
            actualImage = newActual;
            g2.dispose();
        }

        //Compare the two images, writing the result to disk
        ImageComparison comparison = new ImageComparison(testDataImage, actualImage);
        comparison.setAllowingPercentOfDifferentPixels(0.06); //0.06% Will allow for a ~51 pixel difference. A text cursor is ~38
        ImageComparisonResult comparisonResult = comparison.compareImages();
        FileManager.saveImage(outputFolder, resultImageFileName, comparisonResult.getResult());

        return comparisonResult;
    }
}
package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.system.DownloadManager;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

public class UploadSteps {
    protected static final Logger log = LogManager.getLogger(UploadSteps.class.getName()); // Create a logger.

    /**
     * <p>Uploads files whose paths are stored in the testdata section of the page object to a given input element.</p>
     * <p>Multiple testdata file references should be separated by ", "</p>
     * <p>Supports both testdata fileLocation values of just their name for files within src, or a full path if the file has no match within src</p>
     * <p>For use with input elements utilized for uploading files.</p>
     * <ul>
     *     <li>I upload the <b>test</b> file to the upload button</li>
     *     <li>I upload the <b>test1, test2</b> files to the upload button</li>
     * </ul>
     * <p>
     * testdata:
     * <br>-alpha:
     * <br>--test1:
     * <br>---fileLocation: test.jpg
     * <br>--test2:
     * <br>---fileLocation: C:/testNotInSrc.jpg
     * </p>
     * @param elementName String name of the element to upload the file(s) to
     */
    @When("^I upload the (.*?) files? to the (.*?)$")
    public static void sendPathsToInputElement(String testDataFilePaths, String elementName) {
        String[] filePathLocators = testDataFilePaths.split(",");
        List<String> filesToUpload = new ArrayList<>();

        for (String pathLocator : filePathLocators) {
            filesToUpload.add(Configuration.getTestData(pathLocator.trim(), "fileLocation"));
        }

        getElement(elementName).sendFilePaths(filesToUpload);
    }

    /**
     * Uploads the file that was most recently downloaded to a given input element.
     * This method will not work if a file has not been downloaded with the DownloadVerificationSteps / DownloadManager in the current test run.
     * For use with input elements utilized for uploading files.
     * <ul>
     *     <li>I re-upload the most recently downloaded file to the upload button</li>
     * </ul>
     * @param elementName String name of the element to upload the file to
     */
    @When("^I re-upload the most recently downloaded file to the (.*?)$")
    public static void sendMostRecentDownloadToInputElement(String elementName){
        getElement(elementName).sendFilePaths(Collections.singletonList(DownloadManager.getMostRecentDownloadPath().toString()));
    }
}
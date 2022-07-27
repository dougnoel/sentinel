package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

public class FileSteps {
    protected static final Logger log = LogManager.getLogger(ImageSteps.class.getName()); // Create a logger.

    /**
     * Uploads files whose paths are stored in the testdata section of the page object to a given element.
     * <br>
     * For use with input elements utilized for uploading files.
     * <ul>
     *     <li>I upload the <b>test</b> file to the upload button</li>
     *     <li>I upload the <b>test1, test2</b> files to the upload button</li>
     * </ul>
     * @param elementName String name of the element to screenshot
     */
    @When("^I upload the (.*?) files? to the (.*?)$")
    public static void sendPathsToElement(String storedFilePaths, String elementName) {
        String[] filePathLocators = storedFilePaths.split(", ");
        List<File> filesToProcess = new ArrayList<>();

        for (String pathLocator : filePathLocators) {
            String file = Configuration.getTestdataValue(pathLocator, "fileLocation");
            File fileToProcess;

            try {
                fileToProcess = FileManager.findFilePath(file);
                filesToProcess.add(fileToProcess);
            } catch (FileException fileNotFound) {
                fileToProcess = new File(file);
                String errorMessage;
                if (fileToProcess.exists() && !fileToProcess.isDirectory()) {
                    filesToProcess.add(fileToProcess);
                } else {
                    if(fileToProcess.isDirectory())
                        errorMessage = SentinelStringUtils.format("The given {} file was a directory", file);
                    else
                        errorMessage = SentinelStringUtils.format("The {} file could not be found to send to the element {}", file, elementName);

                    log.error(errorMessage);
                    throw new com.dougnoel.sentinel.exceptions.IOException(errorMessage);
                }
            }
        }

        getElement(elementName).sendFilePaths(filesToProcess);
    }
}
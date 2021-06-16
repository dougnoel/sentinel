package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.elements.PageElement;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.When;

public class PDFSteps {

    /**
     * Takes a link name and an extension type. Expects the document to be opened in a new tab/window.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I open the .docx file in a new tab</li>
     * <li>I open the pdf in a new tab"</li>
     * <li>I open the .md file in a new tab</li>
     * </ul>
     * @param linkName String The text of the Download link. NOT a PageElement object name.
     * @param extension String Extension type (.txt/.pdf/.etc) of the file being downloaded.
     * @throws InterruptedException Passes through any errors to the executing code.
     */
    @When("^I open the (.*?)( pdf)? in a new tab$")
    public static void openPDFInNewTab(String linkName, String extension) throws InterruptedException {
    	if (extension == null) {
        	extension = "pdf";
        	getElement(linkName).click();
        } else {
        	extension = StringUtils.strip(extension);
        	//TODO: Unit test. Also, maybe make this easier to do?
        	Map<String,String> linkLocator = new HashMap<>();
        	linkLocator.put("PARTIALTEXT", linkName);
        	new PageElement("Link", linkLocator).click();
        }
        PageManager.waitForPageLoad();
        PageManager.switchToNewWindow();
        String pdfUrl = PageManager.getCurrentUrl();
        String expectedResult = SentinelStringUtils.format("Expected URL \"{}\" to contain the .{} extension.", pdfUrl,
                extension);
        assertTrue(expectedResult, pdfUrl.contains("." + extension));
    }
    
}
